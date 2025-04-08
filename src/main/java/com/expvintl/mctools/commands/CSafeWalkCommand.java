package com.expvintl.mctools.commands;

import com.expvintl.mctools.Globals;
import com.expvintl.mctools.utils.CommandUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CSafeWalkCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        CommandUtils.CreateStatusCommand("cselfwalk",Globals.selfWalk,dispatcher);

        dispatcher.register(literal("cselfwalk").then(argument("开关", BoolArgumentType.bool()).executes(CSafeWalkCommand::execute)));
    }

    private static int execute(CommandContext<FabricClientCommandSource> context) {
        Globals.selfWalk.set(context.getArgument("开关", Boolean.class));
        if(Globals.selfWalk.get()){
            context.getSource().getPlayer().sendMessage(Text.literal("已启用自动挂边!"),false);
        }else{
            context.getSource().getPlayer().sendMessage(Text.literal("已禁用自动挂边!"),false);
        }
        return Command.SINGLE_SUCCESS;
    }
}
