package com.expvintl.mctools.commands;

import com.expvintl.mctools.Globals;
import com.expvintl.mctools.events.MCEventBus;
import com.expvintl.mctools.events.network.PacketSendEvent;
import com.expvintl.mctools.mixin.interfaces.PlayerMoveC2SPacketAccessor;
import com.expvintl.mctools.utils.CommandUtils;
import com.google.common.eventbus.Subscribe;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CNoFallPacketCommand {
    private static final CNoFallPacketCommand INSTANCE=new CNoFallPacketCommand();
    private static final MinecraftClient mc=MinecraftClient.getInstance();
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        MCEventBus.INSTANCE.register(INSTANCE);
        CommandUtils.CreateStatusCommand("cnofallpacket",Globals.noFallPacket,dispatcher);

        dispatcher.register(literal("cnofallpacket").then(argument("开关", BoolArgumentType.bool()).executes(CNoFallPacketCommand::execute)));
    }

    private static int execute(CommandContext<FabricClientCommandSource> context) {
        Globals.noFallPacket.set(context.getArgument("开关", Boolean.class));
        if(Globals.noFallPacket.get()){
            context.getSource().getPlayer().sendMessage(Text.literal("已启用摔落伤害!"),false);
        }else{
            context.getSource().getPlayer().sendMessage(Text.literal("已禁用摔落伤害!"),false);
        }
        return Command.SINGLE_SUCCESS;
    }
    @Subscribe
    private void onSendPacket(PacketSendEvent event){
        //跳过非移动的数据包
        if(!(event.packet instanceof PlayerMoveC2SPacket)) return;
        //跳过创造
        if(Globals.noFallPacket.get()&& !mc.player.getAbilities().creativeMode){
            if(mc.player.fallDistance<=mc.player.getSafeFallDistance()) return;
            if(mc.player.getVelocity().y> -0.5) return;
            //直接发送在地面的数据包来免伤
            ((PlayerMoveC2SPacketAccessor)event.packet).setOnGround(true);
        }
    }
}
