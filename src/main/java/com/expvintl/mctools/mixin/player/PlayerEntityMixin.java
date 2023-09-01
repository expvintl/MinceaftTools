package com.expvintl.mctools.mixin.player;

import com.expvintl.mctools.FeaturesBool;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "clipAtLedge",at = @At("HEAD"), cancellable = true)
    private void onLedge(CallbackInfoReturnable<Boolean> cir){
        if(MinecraftClient.getInstance().world!=null&&MinecraftClient.getInstance().player!=null) {
            if (!MinecraftClient.getInstance().world.isClient) return;
            //挂住边缘
            if (FeaturesBool.selfWalk && !MinecraftClient.getInstance().player.isSneaking()) cir.setReturnValue(true);
        }
    }
}
