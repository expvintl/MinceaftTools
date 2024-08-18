package com.expvintl.mctools.commands;

import com.expvintl.mctools.events.MCEventBus;
import com.expvintl.mctools.events.client.RenderWorldEvent;
import com.expvintl.mctools.utils.Utils;
import com.google.common.eventbus.Subscribe;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CFindBlockCommand {
    private static final MinecraftClient mc=MinecraftClient.getInstance();
    private static final CFindBlockCommand INSTANCE=new CFindBlockCommand();

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        MCEventBus.INSTANCE.register(INSTANCE);
        dispatcher.register(literal("cfindblock").then(argument("方块名字", StringArgumentType.string()).executes(CFindBlockCommand::execute)));
    }
    private static int execute(CommandContext<FabricClientCommandSource> context) {
        String blockName=context.getArgument("方块名字", String.class);
        Utils.findBlock(mc.player,blockName,10);
        return Command.SINGLE_SUCCESS;
    }
    @Subscribe
    private void onRenderWorld(RenderWorldEvent event){

    }
}
