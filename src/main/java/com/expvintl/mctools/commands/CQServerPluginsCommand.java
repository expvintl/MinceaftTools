package com.expvintl.mctools.commands;

import com.expvintl.mctools.FeaturesBool;
import com.expvintl.mctools.events.MCEventBus;
import com.expvintl.mctools.events.network.PacketEvent;
import com.google.common.eventbus.Subscribe;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestion;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import net.minecraft.text.Text;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CQServerPluginsCommand {
    private static final CQServerPluginsCommand INSTANCE=new CQServerPluginsCommand();
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(literal("cqserverplugins").executes(CQServerPluginsCommand::execute));
    }

    private static int execute(CommandContext<FabricClientCommandSource> context) {
        //注册数据包接受事件
        MCEventBus.INSTANCE.register(INSTANCE);
        FeaturesBool.checkBukkitPlugins=true;
        context.getSource().getPlayer().networkHandler.sendPacket(new RequestCommandCompletionsC2SPacket(new Random().nextInt(200),"bukkit:ver "));
        //1秒后关闭避免识别其他命令提示
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                FeaturesBool.checkBukkitPlugins=false;
                MCEventBus.INSTANCE.unregister(INSTANCE);
            }
        },1000);
        return Command.SINGLE_SUCCESS;
    }
    @Subscribe
    public void onReceivePacket(PacketEvent p){
        //探测bukkit服务器插件
        if (!MinecraftClient.getInstance().isIntegratedServerRunning()&&FeaturesBool.checkBukkitPlugins) {
            if (p.packet instanceof CommandSuggestionsS2CPacket sg) {
                StringBuilder buf=new StringBuilder();
                buf.append(String.format("找到%d个插件:",sg.getSuggestions().getList().size())).append('\n');
                for(Suggestion s:sg.getSuggestions().getList()){
                    buf.append('[').append(s.getText()).append(']').append(' ');
                }
                if(MinecraftClient.getInstance().player!=null){
                    MinecraftClient.getInstance().player.sendMessage(Text.literal(buf.toString()));
                }
                FeaturesBool.checkBukkitPlugins=false;
                //取消事件注册
                MCEventBus.INSTANCE.unregister(INSTANCE);
            }
        }
    }
}
