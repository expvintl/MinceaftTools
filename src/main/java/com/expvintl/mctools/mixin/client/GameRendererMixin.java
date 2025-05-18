package com.expvintl.mctools.mixin.client;

import com.expvintl.mctools.modules.CameraZoom;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyReturnValue(method = "getFov",at=@At("RETURN"))
    private double getFov(double original){
        return CameraZoom.INSTANCE.isZoom?20.f:original;
    }
}
