package com.expvintl.mctools.modules;

import com.expvintl.mctools.events.MCEventBus;
import com.expvintl.mctools.events.item.ItemStackTooltipEvent;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class BetterTooltip {
    public static BetterTooltip INSTANCE=new BetterTooltip();
    public void init(){
        MCEventBus.INSTANCE.register(INSTANCE);
    }
    @Subscribe
    public void onGetTooltip(ItemStackTooltipEvent event){

    }
}
