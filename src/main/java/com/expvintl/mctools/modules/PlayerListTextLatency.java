package com.expvintl.mctools.modules;

import com.expvintl.mctools.events.MCEventBus;
import com.expvintl.mctools.events.hud.RenderLatencyIconEvent;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;

public class PlayerListTextLatency {
    public static PlayerListTextLatency INSTANCE=new PlayerListTextLatency();
    public void init(){
        MCEventBus.INSTANCE.register(INSTANCE);
    }
    @Subscribe
    public void onRenderLatencyIcon(RenderLatencyIconEvent event){
        TextRenderer renderer=MinecraftClient.getInstance().textRenderer;
        int latency=Math.clamp(event.entry.getLatency(),0,9999);
        String text=latency+" ms";
        event.draw.drawTextWithShadow(renderer,text, event.x+event.width-renderer.getWidth(text),event.y,0x00E970);
        event.callback.cancel();
    }
}
