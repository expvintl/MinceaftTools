package com.expvintl.mctools.utils;

import com.expvintl.mctools.settingtype.BooleanSetting;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
public class CommandUtils {
    public static void CreateStatusCommand(String cmd, BooleanSetting setting, CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(literal(cmd).executes((context -> {
            context.getSource().getPlayer().sendMessage(Text.literal("当前启用状态: "+setting.getValue()),false);
            return Command.SINGLE_SUCCESS;
        })));
    }
}
