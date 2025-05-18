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
    private int calcLatencyColor(int latency){
        if(latency>=0&&latency<=60){ //0-60
            return 0x00FF00; //绿色
        }else if(latency>60&&latency<=100){ //60-100
            return 0xFFFF00; //黄色
        }else if(latency>100&&latency<=200){//100-200
            return 0xFFA500; //橙色
        }else if(latency>200){ //>200
            return 0xFF0000; //红色
        }
        return 0xFFFFFF; //默认白色
    }
    @Subscribe
    public void onRenderLatencyIcon(RenderLatencyIconEvent event){
        TextRenderer renderer=MinecraftClient.getInstance().textRenderer;
        int latency=Math.clamp(event.entry.getLatency(),0,999);
        String text=latency+" ms";
        event.draw.drawTextWithShadow(renderer,text, event.x+event.width-renderer.getWidth(text),event.y,calcLatencyColor(latency));
        event.callback.cancel();
    }
}
