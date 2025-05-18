package com.expvintl.mctools.commands;

import com.expvintl.mctools.FeaturesSettings;
import com.expvintl.mctools.events.MCEventBus;
import com.expvintl.mctools.events.client.sounds.PlaySoundEvent;
import com.expvintl.mctools.utils.CommandUtils;
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
        CommandUtils.CreateStatusCommand("cautofish", FeaturesSettings.INSTANCE.autoFish, dispatcher);

        dispatcher.register(literal("cautofish").then(argument("开关", BoolArgumentType.bool()).executes(CAutoFishCommand::execute)));
    }

    private static int execute(CommandContext<FabricClientCommandSource> context) {
        FeaturesSettings.INSTANCE.autoFish.setValue(context.getArgument("开关", Boolean.class));
        if(FeaturesSettings.INSTANCE.autoFish.getValue()){
            context.getSource().getPlayer().sendMessage(Text.literal("已启用自动钓鱼!"),false);
        }else{
            context.getSource().getPlayer().sendMessage(Text.literal("已禁用自动钓鱼!"),false);
        }
        return Command.SINGLE_SUCCESS;
    }
    @Subscribe
    private void onPlaySound(PlaySoundEvent event){
        if(FeaturesSettings.INSTANCE.autoFish.getValue()) {
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
