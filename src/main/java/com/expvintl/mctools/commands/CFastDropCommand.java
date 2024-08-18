package com.expvintl.mctools.commands;

import com.expvintl.mctools.mixin.interfaces.SimpleOptionAccessor;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CFastDropCommand {
    public static List<Item> trashItem=new ArrayList<>();
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(literal("cfastdrop").executes(CFastDropCommand::execute));
        trashItem.add(Items.COBBLESTONE);
        trashItem.add(Items.GRANITE);//花岗岩
        trashItem.add(Items.ANDESITE);//安山岩
        trashItem.add(Items.DIORITE);//闪长岩
        trashItem.add(Items.GRAVEL);//沙砾
        trashItem.add(Items.NETHERRACK);//地狱岩
        trashItem.add(Items.BLACKSTONE);//黑石
        trashItem.add(Items.BASALT); //玄武岩
        trashItem.add(Items.DIRT);//泥土
        trashItem.add(Items.PUFFERFISH);//河豚
    }

    private static int execute(CommandContext<FabricClientCommandSource> context) {
        ClientPlayerEntity player=context.getSource().getPlayer();
        if(player==null){
            return 0;
        }
        PlayerInventory inv=player.getInventory();
        for(int i=0;i<inv.main.size();i++) {
            ItemStack item = inv.main.get(i);
            for (Item trash : trashItem) {
                if (item.getItem() == trash) {
                    player.dropStack(item);
                }
            }
        }
        player.sendMessage(Text.literal("已丢弃无用物品!"));
        return Command.SINGLE_SUCCESS;
    }

}
