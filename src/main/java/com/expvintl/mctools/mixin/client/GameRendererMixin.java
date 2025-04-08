package com.expvintl.mctools.mixin.client;

import com.expvintl.mctools.modules.CameraZoom;
import com.expvintl.mctools.utils.Utils;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyReturnValue(method = "getFov",at=@At("RETURN"))
    private float getFov(float origin){
        return CameraZoom.INSTANCE.isZoom?20.f:origin;
    }
}
