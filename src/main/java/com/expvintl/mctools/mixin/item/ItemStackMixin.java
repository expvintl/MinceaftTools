package com.expvintl.mctools.mixin.item;

import com.expvintl.mctools.events.MCEventBus;
import com.expvintl.mctools.events.item.ItemStackTooltipEvent;
import com.expvintl.mctools.utils.Utils;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @ModifyReturnValue(method = "getTooltip",at=@At("RETURN"))
    private List<Text> onGetTooltip(List<Text> list) {
        if(Utils.isReady()) {
            ItemStackTooltipEvent event=ItemStackTooltipEvent.get((ItemStack) (Object) this, list);
            MCEventBus.INSTANCE.post(event);
            return event.textList;
        }
        return list;
    }
}
