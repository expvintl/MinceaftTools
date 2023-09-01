package com.expvintl.mctools.mixin;

import com.expvintl.mctools.FeaturesBool;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftClient.class,priority = 1001)
public class MinecraftClientMixin {
    @Inject(method = "setScreen",at=@At("HEAD"))
    //挂钩设置界面函数
    private void onSetScreen(Screen screen, CallbackInfo info){
        if(FeaturesBool.autoRespawn) {
            //自动重生
            if (screen instanceof DeathScreen) {
                if (MinecraftClient.getInstance().player != null) {
                    MinecraftClient.getInstance().player.requestRespawn();
                }
            }
        }
    }
}
