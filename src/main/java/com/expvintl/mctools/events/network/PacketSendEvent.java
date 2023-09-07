package com.expvintl.mctools.events.network;

import net.minecraft.network.packet.Packet;

public class PacketSendEvent {
    private static final PacketSendEvent INSTANCE=new PacketSendEvent();
    public Packet<?> packet;
    public static PacketSendEvent get(Packet<?> pack){
        INSTANCE.packet=pack;
        return INSTANCE;
    }
}
