package com.expvintl.mctools.commands;

import com.expvintl.mctools.FeaturesBool;
import com.expvintl.mctools.mixin.interfaces.SimpleOptionAccessor;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.text.Text;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CQServerPluginsCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(literal("cqserverplugins").executes(CQServerPluginsCommand::execute));
    }

    private static int execute(CommandContext<FabricClientCommandSource> context) {
        FeaturesBool.checkBukkitPlugins=true;
        context.getSource().getPlayer().networkHandler.sendPacket(new RequestCommandCompletionsC2SPacket(new Random().nextInt(200),"bukkit:ver "));
        //1秒后关闭避免识别其他命令提示
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                FeaturesBool.checkBukkitPlugins=false;
            }
        },1000);
        return Command.SINGLE_SUCCESS;
    }
}
