package com.expvintl.mctools.events.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class RenderLatencyIconEvent {
    public static RenderLatencyIconEvent INSTANCE=new RenderLatencyIconEvent();
    public DrawContext draw;
    public PlayerListEntry entry;
    public CallbackInfo callback;
    public int x,y,width;
    public static RenderLatencyIconEvent get(DrawContext draw,int width,int x,int y,PlayerListEntry entry,CallbackInfo callback){
        INSTANCE.draw=draw;
        INSTANCE.callback=callback;
        INSTANCE.entry=entry;
        INSTANCE.y=y;
        INSTANCE.x=x;
        INSTANCE.width=width;
        return INSTANCE;
    }
}
