package com.expvintl.mctools.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.screen.slot.SlotActionType;

public class PlayerUtils {
    public static void DropItem(int slot){
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.interactionManager==null||mc.player==null) return;
        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot,1, SlotActionType.THROW,mc.player);
    }
}
