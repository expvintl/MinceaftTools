package com.expvintl.mctools.commands;

import com.expvintl.mctools.mixin.interfaces.SimpleOptionAccessor;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CFullbirghtCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("cfullbirght").executes(CFullbirghtCommand::execute));
    }

    private static int execute(CommandContext<FabricClientCommandSource> context) {
        ((SimpleOptionAccessor) (Object) context.getSource().getClient().options.getGamma()).forceSetValue(32767.0);
        context.getSource().getPlayer().sendMessage(Text.literal("已应用高亮"), false);
        return Command.SINGLE_SUCCESS;
    }

}
