package com.expvintl.mctools.modules;

import com.expvintl.mctools.FeaturesSettings;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffects;

public class ClearDarknessEffect {
    public static ClearDarknessEffect INSTANCE=new ClearDarknessEffect();

    public void init(){
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }
    public void tick(MinecraftClient client){
        if(client.player!=null && FeaturesSettings.INSTANCE.noDarkness.getValue()){
            if (client.player.hasStatusEffect(StatusEffects.DARKNESS)) {
                client.player.removeStatusEffect(StatusEffects.DARKNESS);
            }
        }
    }
}
