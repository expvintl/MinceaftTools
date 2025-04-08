package com.expvintl.mctools.commands;

import com.expvintl.mctools.Globals;
import com.expvintl.mctools.events.MCEventBus;
import com.expvintl.mctools.events.player.PlayerAttackBlockEvent;
import com.expvintl.mctools.events.player.PlayerAttackEntityEvent;
import com.expvintl.mctools.events.player.PlayerBreakBlockEvent;
import com.expvintl.mctools.mixin.interfaces.ClientPlayerInteractionManagerAccessor;
import com.expvintl.mctools.utils.CommandUtils;
import com.expvintl.mctools.utils.Utils;
import com.google.common.eventbus.Subscribe;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BambooShootBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

import java.util.function.BiConsumer;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CAutoToolCommand {
    private static final CAutoToolCommand INSTANCE=new CAutoToolCommand();
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        MCEventBus.INSTANCE.register(INSTANCE);
        CommandUtils.CreateStatusCommand("cautotool",Globals.autoTool,dispatcher);
        dispatcher.register(literal("cautotool").then(argument("开关", BoolArgumentType.bool()).executes(CAutoToolCommand::execute)));
    }

    private static int execute(CommandContext<FabricClientCommandSource> context) {
        Globals.autoTool.set(context.getArgument("开关", Boolean.class));
        if(Globals.autoTool.get()){
            context.getSource().getPlayer().sendMessage(Text.literal("已启用智能工具!"),false);
        }else{
            context.getSource().getPlayer().sendMessage(Text.literal("已禁用智能工具!"),false);
        }
        return Command.SINGLE_SUCCESS;
    }

    @Subscribe
    private void onBreakBlock(PlayerBreakBlockEvent event){
        if(!Globals.autoTool.get()) return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if (mc.world == null||mc.player==null) return;
    }
    @Subscribe
    private void onAttackEntity(PlayerAttackEntityEvent event){
        if(!Globals.autoTool.get()) return;
        if(event.target.hasCustomName()) return;
        //不对玩家使用
        if(event.target.isPlayer()) return;
        float bestScore=-1;
        int slot=-1;
        for(int i=0;i<9;i++) {
            ItemStack item = event.player.getInventory().getStack(i);
            if(!isSwordItem(item.getItem())&&!isToolItem(item.getItem())) continue;
            float score=getWeaponScore(item);
            if(score<=0) continue;
            //选出最好分数的工具
            if(score>bestScore){
                bestScore=score;
                slot=i;
            }
        }
        if(slot==-1) return;
        ItemStack currentItem = event.player.getInventory().getStack(slot);
        //低耐久测试
        if(!isLowDurability(currentItem)) {
            //切换过去
            event.player.getInventory().setSelectedSlot(slot);
            MinecraftClient mc=MinecraftClient.getInstance();
            if(mc.interactionManager!=null) {
                ((ClientPlayerInteractionManagerAccessor) mc.interactionManager).syncSelectedSlot();
            }
        }
    }
    //方块挖掘
    @Subscribe
    private void onAttackBlock(PlayerAttackBlockEvent event){
        if(!Globals.autoTool.get()) return;
        //自动工具
        MinecraftClient mc=MinecraftClient.getInstance();
        if (mc.world == null||mc.player==null) return;
        BlockState state= mc.world.getBlockState(event.blockPos);
        //跳过不可破坏
        if(state.getHardness(mc.world, event.blockPos) < 0) return;
        //统计最好的挖掘分数
        float bestScore=-1;
        //工具槽
        int slot=-1;
        //遍历每一个物品槽
        for(int i=0;i<9;i++){
            ItemStack item = mc.player.getInventory().getStack(i);
            float score= getToolsScore(item,state);
            if(score<0) continue;
            //选出最好分数的工具
            if(score>bestScore){
                bestScore=score;
                slot=i;
            }
        }
        if(slot==-1) return;
        ItemStack currentItem = mc.player.getInventory().getStack(slot);
        //确定已经选择好了工具就切换
        if(!isLowDurability(currentItem)) {
            //切换过去
            mc.player.getInventory().setSelectedSlot(slot);
            if(mc.interactionManager!=null) {
                ((ClientPlayerInteractionManagerAccessor) mc.interactionManager).syncSelectedSlot();
            }
        }
    }
    public boolean isSwordItem(Item item){
        return item==Items.STONE_SWORD||item==Items.DIAMOND_SWORD||item==Items.GOLDEN_SWORD||item==Items.IRON_SWORD||item==Items.NETHERITE_SWORD||item==Items.WOODEN_SWORD;
    }
    public boolean isToolItem(Item item){
        return item == Items.WOODEN_PICKAXE || item == Items.STONE_PICKAXE || item == Items.IRON_PICKAXE || item == Items.GOLDEN_PICKAXE || item == Items.DIAMOND_PICKAXE || item == Items.NETHERITE_PICKAXE
                || item == Items.WOODEN_AXE || item == Items.STONE_AXE || item == Items.IRON_AXE || item == Items.GOLDEN_AXE || item == Items.DIAMOND_AXE || item == Items.NETHERITE_AXE
                || item == Items.WOODEN_SHOVEL || item == Items.STONE_SHOVEL || item == Items.IRON_SHOVEL || item == Items.GOLDEN_SHOVEL || item == Items.DIAMOND_SHOVEL || item == Items.NETHERITE_SHOVEL
                || item == Items.WOODEN_HOE || item == Items.STONE_HOE || item == Items.IRON_HOE || item == Items.GOLDEN_HOE || item == Items.DIAMOND_HOE || item == Items.NETHERITE_HOE
                || item == Items.WOODEN_SWORD || item == Items.STONE_SWORD || item == Items.IRON_SWORD || item == Items.GOLDEN_SWORD || item == Items.DIAMOND_SWORD || item == Items.NETHERITE_SWORD;
    }
    public float getToolsScore(ItemStack item, BlockState state){
        float score=0;
        if(isToolItem(item.getItem())||item.getItem() instanceof ShearsItem){
            //根据挖掘速度提升评分
            score+=item.getMiningSpeedMultiplier(state)*30;
            //附魔加分
            //耐久
            score+= Utils.GetEnchantLevel(Enchantments.UNBREAKING, item);
            //效率
            score+=Utils.GetEnchantLevel(Enchantments.EFFICIENCY,item);
            //经验修补
            score+=Utils.GetEnchantLevel(Enchantments.MENDING,item);
            if (isSwordItem(item.getItem()) && (state.getBlock() instanceof BambooBlock|| state.getBlock() instanceof BambooShootBlock)) {
                if((item.getItem().getComponents().get(DataComponentTypes.TOOL)!=null)){
                    //根据挖掘等级加分
                    score += 90 + (item.getItem().getComponents().get(DataComponentTypes.TOOL).getSpeed(state) * 10);
                }
            }
        }
        return score;
    }
    public float getWeaponScore(ItemStack item) {
        float damageScore = 0;
        //剑优先
        if (isSwordItem(item.getItem())) damageScore += 100;
        AttributeModifiersComponent comp=item.getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT);
        final float[] damageHolder = {0.0f};
        BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> baseDamage=(attentry, modify)->{
            if(attentry.matches(EntityAttributes.ATTACK_DAMAGE)){
                //计算基础伤害
                damageHolder [0]= (float)modify.value();
            }
        };
        comp.applyModifiers(EquipmentSlot.MAINHAND,baseDamage);
        damageScore+=damageHolder[0];
        //锋利加分
        damageScore += Utils.GetEnchantLevel(Enchantments.SHARPNESS, item) * 2;
        //精修
        damageScore += Utils.GetEnchantLevel(Enchantments.MENDING, item);
        //火焰附加
        damageScore += Utils.GetEnchantLevel(Enchantments.FIRE_ASPECT, item) * 3;
        //击退
        damageScore += Utils.GetEnchantLevel(Enchantments.KNOCKBACK, item) * 2;
        return damageScore;
    }
    //停用低耐久度
    private boolean isLowDurability(ItemStack itemStack) {
        Item item = itemStack.getItem();
        boolean isWooden = item == Items.WOODEN_SWORD || item == Items.WOODEN_PICKAXE ||
                item == Items.WOODEN_AXE || item == Items.WOODEN_SHOVEL ||
                item == Items.WOODEN_HOE;
        boolean isStone = item == Items.STONE_SWORD || item == Items.STONE_PICKAXE ||
                item == Items.STONE_AXE || item == Items.STONE_SHOVEL ||
                item == Items.STONE_HOE;
        boolean isIron = item == Items.IRON_SWORD || item == Items.IRON_PICKAXE ||
                item == Items.IRON_AXE || item == Items.IRON_SHOVEL ||
                item == Items.IRON_HOE;
        return  !(isWooden||isStone||isIron) //忽略木/石/铁工具
                &&(itemStack.getMaxDamage() - itemStack.getDamage()) < (itemStack.getMaxDamage() * 10 / 100);
    }
}
