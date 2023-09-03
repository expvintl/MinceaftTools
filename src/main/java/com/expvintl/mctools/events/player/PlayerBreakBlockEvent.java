package com.expvintl.mctools.events.player;

import net.minecraft.util.math.BlockPos;

public class PlayerBreakBlockEvent {
    private static final PlayerBreakBlockEvent INSTANCE=new PlayerBreakBlockEvent();
    public BlockPos pos;

    public static PlayerBreakBlockEvent get(BlockPos blockPos){
        INSTANCE.pos=blockPos;
        return INSTANCE;
    }
}
