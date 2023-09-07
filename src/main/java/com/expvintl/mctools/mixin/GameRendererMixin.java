package com.expvintl.mctools.mixin;

import com.expvintl.mctools.events.MCEventBus;
import com.expvintl.mctools.events.client.RenderWorldEvent;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Inject(method = "renderWorld",at=@At(value = "INVOKE_STRING",target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",args = {"ldc=hand"}),locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onRenderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci, MatrixStack matrices1, boolean bl, Camera camera, MatrixStack matrixStack, double d, float f, float g, Matrix4f matrix4f, Matrix3f matrix3f){
        MCEventBus.INSTANCE.post(RenderWorldEvent.get(tickDelta,camera,matrices));
    }
}
