package com.expvintl.mctools.events.item;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public class ItemStackTooltipEvent {
    public static ItemStackTooltipEvent INSTANCE=new ItemStackTooltipEvent();
    public ItemStack item;
    public List<Text> textList;

    public static ItemStackTooltipEvent get(ItemStack item,List<Text> list){
        INSTANCE.item=item;
        INSTANCE.textList=list;
        return INSTANCE;
    }
}
