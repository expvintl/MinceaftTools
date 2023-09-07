package com.expvintl.mctools.mixin.network;

import com.expvintl.mctools.events.MCEventBus;
import com.expvintl.mctools.events.network.PacketReceiveEvent;
import com.expvintl.mctools.events.network.PacketSendEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "handlePacket",at=@At("HEAD"))
    private static <T extends PacketListener> void onHandlePacket(Packet<T> packet, PacketListener listener, CallbackInfo info) {
        //传入事件
        MCEventBus.INSTANCE.post(PacketReceiveEvent.get(packet));
    }
    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V",at=@At("TAIL"))
    private void onSendPakcet(Packet<?> packet, CallbackInfo ci){
        MCEventBus.INSTANCE.post(PacketSendEvent.get(packet));
    }
}
