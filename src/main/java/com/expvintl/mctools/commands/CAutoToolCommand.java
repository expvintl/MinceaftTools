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
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CAutoToolCommand {
    private static final CAutoToolCommand INSTANCE=new CAutoToolCommand();
    private int lastSlot=-1;
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        MCEventBus.INSTANCE.register(INSTANCE);
        CommandUtils.CreateStatusCommand("cautotool",Globals.autoTool,dispatcher);
        dispatcher.register(literal("cautotool").then(argument("开关", BoolArgumentType.bool()).executes(CAutoToolCommand::execute)));
    }

    private static int execute(CommandContext<FabricClientCommandSource> context) {
        Globals.autoTool.set(context.getArgument("开关", Boolean.class));
        if(Globals.autoTool.get()){
            context.getSource().getPlayer().sendMessage(Text.literal("已启用智能工具!"));
        }else{
            context.getSource().getPlayer().sendMessage(Text.literal("已禁用智能工具!"));
        }
        return Command.SINGLE_SUCCESS;
    }
    @Subscribe
    private void onBreakBlock(PlayerBreakBlockEvent event){
        if(!Globals.autoTool.get()) return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if (mc.world == null||mc.player==null) return;
        if (lastSlot!=-1){
            //破坏方块后切换回去
            mc.player.getInventory().selectedSlot=lastSlot;
            lastSlot=-1;
        }
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
            float score=getWeaponScore(event.player,event.target,i);
            if(score<0) continue;
            //选出最好分数的工具
            if(score>bestScore){
                bestScore=score;
                slot=i;
            }
        }
        if(slot==-1) return;
        ItemStack currentItem = event.player.getInventory().getStack(slot);
        //低耐久测试
        if(!lowDurability(currentItem)) {
            //切换过去
            event.player.getInventory().selectedSlot = slot;
            MinecraftClient mc=MinecraftClient.getInstance();
            if(mc.interactionManager!=null) {
                ((ClientPlayerInteractionManagerAccessor) mc.interactionManager).syncSelectedSlot();
            }
        }
    }
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
        if(!lowDurability(currentItem)) {
            //记住上一次的槽方便恢复
            lastSlot=mc.player.getInventory().selectedSlot;
            //切换过去
            mc.player.getInventory().selectedSlot = slot;
            if(mc.interactionManager!=null) {
                ((ClientPlayerInteractionManagerAccessor) mc.interactionManager).syncSelectedSlot();
            }
        }
    }
    public float getToolsScore(ItemStack item, BlockState state){
        float score=0;
        if(item.getItem() instanceof ToolItem || item.getItem() instanceof ShearsItem){
            //根据挖掘速度提升评分
            score+=item.getMiningSpeedMultiplier(state)*30;
            //附魔加分
            //耐久
            score+= EnchantmentHelper.getLevel(Enchantments.UNBREAKING, item);
            //效率
            score+=EnchantmentHelper.getLevel(Enchantments.EFFICIENCY,item);
            //经验修补
            score+=EnchantmentHelper.getLevel(Enchantments.MENDING,item);
            if (item.getItem() instanceof SwordItem item1 && (state.getBlock() instanceof BambooBlock|| state.getBlock() instanceof BambooShootBlock))
                //根据挖掘等级加分
                score += 90 + (item1.getMaterial().getMiningSpeedMultiplier() * 10);
        }
        return score;
    }
    public float getWeaponScore(PlayerEntity player,Entity target,int slot) {
        float damageScore = 0;
        ItemStack item = player.getInventory().getStack(slot);
        //剑优先
        if(item.getItem() instanceof SwordItem) damageScore+=10;
        //使用所有工具组
        if (item.getItem() instanceof ToolItem tool) {
            damageScore += tool.getMaterial().getAttackDamage();
            //锋利加分
            damageScore += EnchantmentHelper.getLevel(Enchantments.SHARPNESS, item) * 2;
            //精修
            damageScore+=EnchantmentHelper.getLevel(Enchantments.MENDING,item);
            //火焰附加
            damageScore+=EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT,item)*3;
            //击退
            damageScore+=EnchantmentHelper.getLevel(Enchantments.KNOCKBACK,item)*2;
        }
        return damageScore;
    }
    //停用低耐久度
    private boolean lowDurability(ItemStack itemStack) {
        return  (itemStack.getMaxDamage() - itemStack.getDamage()) < (itemStack.getMaxDamage() * 10 / 100);
    }
}
