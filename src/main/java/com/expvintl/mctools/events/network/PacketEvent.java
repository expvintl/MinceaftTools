package com.expvintl.mctools.events.network;

import net.minecraft.network.packet.Packet;

public class PacketEvent {
    private static final PacketEvent INSTANCE=new PacketEvent();
    public Packet<?> packet;
    public static PacketEvent get(Packet<?> pack){
        INSTANCE.packet=pack;
        return INSTANCE;
    }
}
