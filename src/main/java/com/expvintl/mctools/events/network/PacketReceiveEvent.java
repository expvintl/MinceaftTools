package com.expvintl.mctools.events.network;

import net.minecraft.network.packet.Packet;

public class PacketReceiveEvent {
    private static final PacketReceiveEvent INSTANCE=new PacketReceiveEvent();
    public Packet<?> packet;
    public static PacketReceiveEvent get(Packet<?> pack){
        INSTANCE.packet=pack;
        return INSTANCE;
    }
}
