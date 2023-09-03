package com.expvintl.mctools.events.client.sounds;

import net.minecraft.client.sound.SoundInstance;

public class PlaySoundEvent {
    private static final PlaySoundEvent INSTANCE=new PlaySoundEvent();
    public SoundInstance soundInstance;
    public static PlaySoundEvent get(SoundInstance instance){
        INSTANCE.soundInstance=instance;
        return INSTANCE;
    }
}
