package com.expvintl.mctools.commands;

import com.expvintl.mctools.FeaturesSettings;
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
        CommandUtils.CreateStatusCommand("cselfwalk", FeaturesSettings.INSTANCE.safeWalk, dispatcher);

        dispatcher.register(literal("cselfwalk").then(argument("开关", BoolArgumentType.bool()).executes(CSafeWalkCommand::execute)));
    }

    private static int execute(CommandContext<FabricClientCommandSource> context) {
        FeaturesSettings.INSTANCE.safeWalk.setValue(context.getArgument("开关", Boolean.class));
        if(FeaturesSettings.INSTANCE.safeWalk.getValue()){
            context.getSource().getPlayer().sendMessage(Text.literal("已启用自动挂边!"),false);
        }else{
            context.getSource().getPlayer().sendMessage(Text.literal("已禁用自动挂边!"),false);
        }
        return Command.SINGLE_SUCCESS;
    }
}
