package com.expvintl.mctools.events.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class PlayerAttackEntityEvent {
    private static final PlayerAttackEntityEvent INSTANCE=new PlayerAttackEntityEvent();
    public PlayerEntity player;
    public Entity target;
    public static PlayerAttackEntityEvent get(PlayerEntity playerEntity, Entity target){
        INSTANCE.player=playerEntity;
        INSTANCE.target=target;
        return INSTANCE;
    }
}
