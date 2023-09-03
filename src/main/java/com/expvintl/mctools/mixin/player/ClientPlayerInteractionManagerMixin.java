package com.expvintl.mctools.mixin.player;

import com.expvintl.mctools.events.MCEventBus;
import com.expvintl.mctools.events.player.PlayerAttackBlockEvent;
import com.expvintl.mctools.events.player.PlayerBreakBlockEvent;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Inject(method = "breakBlock",at=@At("HEAD"))
    private void breakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir){
        MCEventBus.INSTANCE.post(PlayerBreakBlockEvent.get(pos));
    }
    @Inject(method = "attackBlock",at=@At("HEAD"))
    private void onAttackBlock(BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> info){
        MCEventBus.INSTANCE.post(PlayerAttackBlockEvent.get(blockPos,direction));
    }
}
