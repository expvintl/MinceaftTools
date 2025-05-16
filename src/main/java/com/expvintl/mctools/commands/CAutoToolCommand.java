package com.expvintl.mctools.commands;

import com.expvintl.mctools.Globals;
import com.expvintl.mctools.events.MCEventBus;
import com.expvintl.mctools.events.player.PlayerAttackBlockEvent;
import com.expvintl.mctools.events.player.PlayerAttackEntityEvent;
import com.expvintl.mctools.events.player.PlayerBreakBlockEvent;
import com.expvintl.mctools.mixin.interfaces.ClientPlayerInteractionManagerAccessor;
import com.expvintl.mctools.utils.CommandUtils;
import com.expvintl.mctools.utils.Utils;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.Subscribe;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.text.Text;

import java.util.Collection;
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
            float score=getWeaponScore(event.target, item);
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
            event.player.getInventory().selectedSlot=slot;
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
            if(score<=0) continue;
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
            mc.player.getInventory().selectedSlot=slot;
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
    public boolean isOreBlock(Item item) {
        return item == Items.COAL_ORE ||           // 煤矿石
                item == Items.DEEPSLATE_COAL_ORE ||   // 深层煤矿石
                item == Items.IRON_ORE ||           // 铁矿石
                item == Items.DEEPSLATE_IRON_ORE ||   // 深层铁矿石
                item == Items.COPPER_ORE ||         // 铜矿石
                item == Items.DEEPSLATE_COPPER_ORE || // 深层铜矿石
                item == Items.GOLD_ORE ||           // 金矿石
                item == Items.DEEPSLATE_GOLD_ORE ||   // 深层金矿石
                item == Items.REDSTONE_ORE ||       // 红石矿石
                item == Items.DEEPSLATE_REDSTONE_ORE ||// 深层红石矿石
                item == Items.EMERALD_ORE ||        // 绿宝石矿石
                item == Items.DEEPSLATE_EMERALD_ORE ||// 深层绿宝石矿石
                item == Items.LAPIS_ORE ||          // 青金石矿石
                item == Items.DEEPSLATE_LAPIS_ORE ||  // 深层青金石矿石
                item == Items.DIAMOND_ORE ||        // 钻石矿石
                item == Items.DEEPSLATE_DIAMOND_ORE ||// 深层钻石矿石
                item == Items.NETHER_GOLD_ORE ||    // 下界金矿石
                item == Items.NETHER_QUARTZ_ORE;  // 下界石英矿石
    }
    private boolean isBlockFortune(Block block) {
        if (block == Blocks.COAL_ORE || block == Blocks.DEEPSLATE_COAL_ORE ||
                block == Blocks.COPPER_ORE || block == Blocks.DEEPSLATE_COPPER_ORE ||
                block == Blocks.IRON_ORE || block == Blocks.DEEPSLATE_IRON_ORE ||
                block == Blocks.GOLD_ORE || block == Blocks.DEEPSLATE_GOLD_ORE ||
                block == Blocks.REDSTONE_ORE || block == Blocks.DEEPSLATE_REDSTONE_ORE ||
                block == Blocks.LAPIS_ORE || block == Blocks.DEEPSLATE_LAPIS_ORE ||
                block == Blocks.DIAMOND_ORE || block == Blocks.DEEPSLATE_DIAMOND_ORE ||
                block == Blocks.EMERALD_ORE || block == Blocks.DEEPSLATE_EMERALD_ORE ||
                block == Blocks.NETHER_QUARTZ_ORE || block == Blocks.NETHER_GOLD_ORE ||
                block == Blocks.AMETHYST_CLUSTER) { // 紫水晶簇也受时运影响
            return true;
        }

        if (block == Blocks.WHEAT || // 小麦
                block == Blocks.CARROTS || // 胡萝卜
                block == Blocks.POTATOES || // 马铃薯
                block == Blocks.BEETROOTS || // 甜菜根
                block == Blocks.NETHER_WART) { // 下界疣
            return true;
        }

        if (block == Blocks.GLOWSTONE || // 荧石
                block == Blocks.MELON || // 西瓜
                block == Blocks.GRAVEL || // 沙砾 (影响燧石掉落概率)
                block == Blocks.SEA_LANTERN){ // 海晶灯
            return true;
        }
        return false;
    }
    public float getToolsScore(ItemStack item, BlockState state){
        float score=0;
        if(isToolItem(item.getItem())||item.getItem() instanceof ShearsItem){
            //根据挖掘速度提升评分
            score+=item.getMiningSpeedMultiplier(state)*2;
            //附魔加分
            //耐久
            score+= EnchantmentHelper.getLevel(Enchantments.UNBREAKING, item);
            //效率
            score+=EnchantmentHelper.getLevel(Enchantments.EFFICIENCY,item);
            //经验修补
            score+=EnchantmentHelper.getLevel(Enchantments.MENDING,item);

            if(isBlockFortune(state.getBlock())){
                score+=EnchantmentHelper.getLevel(Enchantments.FORTUNE,item);//时运
            }

            if (isSwordItem(item.getItem()) && (state.getBlock() instanceof BambooBlock)) {
                if(item.getItem() instanceof ToolItem tool){
                    //根据挖掘等级加分
                    score += 90 + tool.getMaterial().getMiningSpeedMultiplier();
                }
            }
        }
        return score;
    }
    public float getWeaponScore(Entity ent, ItemStack item) {
        float damageScore = 0;
        //剑优先
        if (isSwordItem(item.getItem())) damageScore += 100;
        //计算物品的基础伤害属性(较为复杂)
        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = item.getAttributeModifiers(EquipmentSlot.MAINHAND);
        Collection<EntityAttributeModifier> damageModifiers = modifiers.get(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        for (EntityAttributeModifier modifier : damageModifiers) {
            if (modifier.getOperation() == EntityAttributeModifier.Operation.ADDITION) {
                damageScore+=(float) modifier.getValue();
                break;
            }
        }
        //节肢杀手
        EntityType<?> id=ent.getType();
        if(id==EntityType.SPIDER||id==EntityType.CAVE_SPIDER||id==EntityType.SILVERFISH||id==EntityType.ENDERMITE||id==EntityType.BEE) {
            damageScore += EnchantmentHelper.getLevel(Enchantments.BANE_OF_ARTHROPODS, item) * 3;
        }
        //亡灵杀手(这伤害通常更高)
        if(((LivingEntity)ent).getGroup()==EntityGroup.UNDEAD){
            damageScore+=EnchantmentHelper.getLevel(Enchantments.SMITE,item)*3;// 3倍
        }
        //锋利加分
        damageScore += EnchantmentHelper.getLevel(Enchantments.SHARPNESS, item) * 2;
        //精修
        damageScore += EnchantmentHelper.getLevel(Enchantments.MENDING, item);
        //火焰附加
        damageScore += EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, item);
        //击退
        damageScore += EnchantmentHelper.getLevel(Enchantments.KNOCKBACK, item);
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
                &&(itemStack.getMaxDamage() - itemStack.getDamage()) < (itemStack.getMaxDamage() * 2 / 100);
    }
}
