package com.expvintl.mctools.commands;

import com.expvintl.mctools.utils.PlayerUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CFastDropCommand {
    public static List<Item> trashItem=new ArrayList<>();
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher , CommandRegistryAccess access){
        dispatcher.register(literal("cfastdrop").executes(CFastDropCommand::execute));
        dispatcher.register(literal("cfastdrop").then(literal("list").executes((context -> {
            StringBuilder sb=new StringBuilder();
            sb.append("已添加:\n");
            for(Item i:trashItem){
                sb.append(i.getName().getString()).append(",");
            }
            context.getSource().getPlayer().sendMessage(Text.literal(sb.toString()),false);
            return Command.SINGLE_SUCCESS;
        }))));
        dispatcher.register(literal("cfastdrop").then(literal("clear").executes((context -> {
            trashItem.clear();
            context.getSource().getPlayer().sendMessage(Text.literal("已清除全部物品!"),false);
            return Command.SINGLE_SUCCESS;
        }))));
        dispatcher.register(literal("cfastdrop").then(literal("del").then(argument("物品", ItemStackArgumentType.itemStack(access)).suggests(((context, builder) -> suggestItems(builder))).executes(cmd->{
            Item item=ItemStackArgumentType.getItemStackArgument(cmd,"物品").getItem();
            if (item != Items.AIR) { // 确保找到的物品是有效的
                if (trashItem.contains(item)) {
                    trashItem.remove(item);
                    cmd.getSource().getPlayer().sendMessage(Text.literal("已移除 " + item.getName().getString()),false);
                }else{
                    cmd.getSource().getPlayer().sendMessage(Text.literal("没有找到 " + item.getName().getString()),false);
                }
            }else{
                cmd.getSource().getPlayer().sendMessage(Text.literal("无效物品!"),false);
                return 0;
            }
            return Command.SINGLE_SUCCESS;
        }))));
        dispatcher.register(literal("cfastdrop").then(literal("add").then(argument("物品", ItemStackArgumentType.itemStack(access)).suggests(((context, builder) -> suggestItems(builder))).executes(cmd->{
            Item item=ItemStackArgumentType.getItemStackArgument(cmd,"物品").getItem();
            if (item != Items.AIR) { // 确保找到的物品是有效的
                if(trashItem.contains(item)){
                    cmd.getSource().getPlayer().sendMessage(Text.literal( item.getName().getString() + " 已存在!"),false);
                }else {
                    trashItem.add(item);
                    cmd.getSource().getPlayer().sendMessage(Text.literal("已添加 " + item.getName().getString() + " 到垃圾物品列表"),false);
                }
            }else{
                cmd.getSource().getPlayer().sendMessage(Text.literal("无效物品!"),false);
                return 0;
            }
            return Command.SINGLE_SUCCESS;
        }))));
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
        trashItem.add(Items.DANDELION);
        trashItem.add(Items.SUNFLOWER);
        trashItem.add(Items.CORNFLOWER);
        trashItem.add(Items.TUFF); //凝灰岩
        trashItem.add(Items.DEEPSLATE); //深板岩
        trashItem.add(Items.SANDSTONE); //砂岩
        trashItem.add(Items.DEEPSLATE_BRICKS);//深板岩砖
        trashItem.add(Items.COBBLED_DEEPSLATE); //深板岩圆石
    }
    private static CompletableFuture<Suggestions> suggestItems(SuggestionsBuilder builder) {
        // 遍历所有注册的物品并将其添加到建议列表中
        Registries.ITEM.getIds().forEach(id -> builder.suggest(id.toString()));
        return builder.buildFuture();
    }
    private static int execute(CommandContext<FabricClientCommandSource> context) {
        ClientPlayerEntity player=context.getSource().getPlayer();
        if(player==null){
            return 0;
        }
        PlayerInventory inv=player.getInventory();
        for(int i=0;i<inv.size();i++) {
            ItemStack item = inv.getStack(i);
            for (Item trash : trashItem) {
                if (item.getItem() == trash) {
                    PlayerUtils.DropItem(i);
                }
            }
        }
        player.sendMessage(Text.literal("已丢弃无用物品!"),false);
        return Command.SINGLE_SUCCESS;
    }

}
