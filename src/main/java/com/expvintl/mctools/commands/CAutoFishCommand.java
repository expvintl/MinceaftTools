package com.expvintl.mctools.commands;

import com.expvintl.mctools.FeaturesBool;
import com.expvintl.mctools.events.MCEventBus;
import com.expvintl.mctools.events.client.sounds.PlaySoundEvent;
import com.expvintl.mctools.utils.Utils;
import com.google.common.eventbus.Subscribe;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import java.util.TimerTask;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CAutoFishCommand {
    private static final CAutoFishCommand INSTANCE=new CAutoFishCommand();
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        MCEventBus.INSTANCE.register(INSTANCE);
        dispatcher.register(literal("cautofish").then(argument("开关", BoolArgumentType.bool()).executes(CAutoFishCommand::execute)));
    }

    private static int execute(CommandContext<FabricClientCommandSource> context) {
        FeaturesBool.autoFish=context.getArgument("开关", Boolean.class);
        if(FeaturesBool.autoFish){
            context.getSource().getPlayer().sendMessage(Text.literal("已启用自动钓鱼!"));
        }else{
            context.getSource().getPlayer().sendMessage(Text.literal("已禁用自动钓鱼!"));
        }
        return Command.SINGLE_SUCCESS;
    }
    @Subscribe
    private void onPlaySound(PlaySoundEvent event){
        if(FeaturesBool.autoFish) {
            //自动钓鱼
            if (event.soundInstance.getId().getPath().equals("entity.fishing_bobber.splash")) {
                //收杆
                Utils.rightClick();
                Utils.timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Utils.rightClick();
                    }
                },300);

            }
        }
    }
}
