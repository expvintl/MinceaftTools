package com.expvintl.mctools.mixin.player;

import com.expvintl.mctools.mixin.interfaces.IClientPlayerInteractionManager;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BambooSaplingBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin implements IClientPlayerInteractionManager {
    @Shadow
    protected abstract void syncSelectedSlot();
    @Inject(method = "attackBlock",at=@At("HEAD"))
    private void onAttackBlock(BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> info){
        //自动工具
        MinecraftClient mc=MinecraftClient.getInstance();
        if (mc.world == null) return;
        BlockState state= mc.world.getBlockState(blockPos);
        if(state.getHardness(mc.world, blockPos) < 0) return;
        if(mc.player==null) return;
        ItemStack currentItem = mc.player.getMainHandStack();
        double bestScore=-1;
        int slot=0;
        for(int i=0;i<9;i++){
            ItemStack item = mc.player.getInventory().getStack(i);
            //Is Tool!!
            if(item.getItem() instanceof ToolItem || item.getItem() instanceof ShearsItem){
                double score=0;
                score+=item.getMiningSpeedMultiplier(state)*1000;
                if (item.getItem() instanceof SwordItem item1 && (state.getBlock() instanceof BambooBlock || state.getBlock() instanceof BambooSaplingBlock))
                    score += 9000 + (item1.getMaterial().getMiningLevel() * 1000);
                if(score<0) continue;
                if(score>bestScore){
                    bestScore=score;
                    slot=i;
                }
            }
        }
        mc.player.getInventory().selectedSlot=slot;
        ((IClientPlayerInteractionManager)mc.interactionManager).syncSelected();
    }
    @Override
    public void syncSelected() {
        syncSelectedSlot();
    }
}
