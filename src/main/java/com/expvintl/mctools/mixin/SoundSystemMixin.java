package com.expvintl.mctools.mixin;

import com.expvintl.mctools.events.MCEventBus;
import com.expvintl.mctools.events.client.sounds.PlaySoundEvent;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundSystem.class)
public class SoundSystemMixin {
    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V",at=@At("HEAD"))
    private void onPlaySound(SoundInstance instance, CallbackInfo ci){
        MCEventBus.INSTANCE.post(PlaySoundEvent.get(instance));
    }
}
