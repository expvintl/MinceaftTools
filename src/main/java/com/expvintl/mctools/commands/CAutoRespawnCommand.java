package com.expvintl.mctools.commands;

import com.expvintl.mctools.Globals;
import com.expvintl.mctools.events.MCEventBus;
import com.expvintl.mctools.events.client.OpenScreenEvent;
import com.expvintl.mctools.utils.CommandUtils;
import com.google.common.eventbus.Subscribe;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CAutoRespawnCommand {
    private static final CAutoRespawnCommand INSTANCE=new CAutoRespawnCommand();
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        MCEventBus.INSTANCE.register(INSTANCE);
        CommandUtils.CreateStatusCommand("cautorespawn",Globals.autoRespawn,dispatcher);
        dispatcher.register(literal("cautorespawn").then(argument("开关", BoolArgumentType.bool()).executes(CAutoRespawnCommand::execute)));
    }

    private static int execute(CommandContext<FabricClientCommandSource> context) {
        Globals.autoRespawn.set(context.getArgument("开关", Boolean.class));
        if(Globals.autoRespawn.get()){
            context.getSource().getPlayer().sendMessage(Text.literal("已启用自动重生!"),false);
        }else{
            context.getSource().getPlayer().sendMessage(Text.literal("已禁用自动重生!"),false);
        }
        return Command.SINGLE_SUCCESS;
    }
    @Subscribe
    private void onOpenScreen(OpenScreenEvent event){
        if(Globals.autoRespawn.get()) {
            //自动重生
            if (event.screen instanceof DeathScreen) {
                if (MinecraftClient.getInstance().player != null) {
                    MinecraftClient.getInstance().player.requestRespawn();
                }
            }
        }
    }
}
