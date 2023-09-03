package com.expvintl.mctools.events.player;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class PlayerAttackBlockEvent {
    private static final PlayerAttackBlockEvent INSTANCE=new PlayerAttackBlockEvent();
    public BlockPos blockPos;
    public Direction direction;
    public static PlayerAttackBlockEvent get(BlockPos blockPos, Direction direction){
        INSTANCE.blockPos=blockPos;
        INSTANCE.direction=direction;
        return INSTANCE;
    }
}
