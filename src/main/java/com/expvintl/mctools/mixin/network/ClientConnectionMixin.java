package com.expvintl.mctools.mixin.network;

import com.expvintl.mctools.FeaturesBool;
import com.google.common.eventbus.EventBus;
import com.mojang.brigadier.suggestion.Suggestion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.BundlePacket;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.network.packet.s2c.play.BundleS2CPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(method = "handlePacket",at=@At("HEAD"))
    private static <T extends PacketListener> void onHandlePacket(Packet<T> packet, PacketListener listener, CallbackInfo info) {
        //检查bukkit服务器插件
        if (!MinecraftClient.getInstance().isIntegratedServerRunning()&&FeaturesBool.checkBukkitPlugins) {
            if (packet instanceof CommandSuggestionsS2CPacket sg) {
                StringBuilder buf=new StringBuilder();
                buf.append(String.format("找到%d个插件:",sg.getSuggestions().getList().size())).append('\n');
                for(Suggestion s:sg.getSuggestions().getList()){
                    buf.append('[').append(s.getText()).append(']').append(' ');
                }
                if(MinecraftClient.getInstance().player!=null){
                    MinecraftClient.getInstance().player.sendMessage(Text.literal(buf.toString()));
                }
                FeaturesBool.checkBukkitPlugins=false;
            }
        }
    }
}
