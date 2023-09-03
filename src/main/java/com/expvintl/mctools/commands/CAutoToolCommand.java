package com.expvintl.mctools.commands;

import com.expvintl.mctools.FeaturesBool;
import com.expvintl.mctools.events.MCEventBus;
import com.expvintl.mctools.events.player.PlayerAttackBlockEvent;
import com.expvintl.mctools.events.player.PlayerBreakBlockEvent;
import com.expvintl.mctools.mixin.interfaces.ClientPlayerInteractionManagerAccessor;
import com.google.common.eventbus.Subscribe;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BambooSaplingBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CAutoToolCommand {
    private static final CAutoToolCommand INSTANCE=new CAutoToolCommand();
    private int lastSlot=-1;
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        MCEventBus.INSTANCE.register(INSTANCE);
        dispatcher.register(literal("cautotool").then(argument("开关", BoolArgumentType.bool()).executes(CAutoToolCommand::execute)));
    }

    private static int execute(CommandContext<FabricClientCommandSource> context) {
        FeaturesBool.autoTool=context.getArgument("开关", Boolean.class);
        if(FeaturesBool.autoTool){
            context.getSource().getPlayer().sendMessage(Text.literal("已启用智能工具!"));
        }else{
            context.getSource().getPlayer().sendMessage(Text.literal("已禁用智能工具!"));
        }
        return Command.SINGLE_SUCCESS;
    }
    @Subscribe
    private void onBreakBlock(PlayerBreakBlockEvent event){
        if(!FeaturesBool.autoTool) return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if (mc.world == null||mc.player==null) return;
        if (lastSlot!=-1){
            //破坏方块后切换回去
            mc.player.getInventory().selectedSlot=lastSlot;
        }
    }
    @Subscribe
    private void onAttackBlock(PlayerAttackBlockEvent event){
        if(!FeaturesBool.autoTool) return;
        //自动工具
        MinecraftClient mc=MinecraftClient.getInstance();
        if (mc.world == null||mc.player==null) return;
        BlockState state= mc.world.getBlockState(event.blockPos);
        //跳过不可破坏
        if(state.getHardness(mc.world, event.blockPos) < 0) return;
        ItemStack currentItem = mc.player.getMainHandStack();
        //统计最好的挖掘分数
        double bestScore=-1;
        //工具槽
        int slot=-1;
        //遍历每一个物品槽
        for(int i=0;i<9;i++){
            ItemStack item = mc.player.getInventory().getStack(i);
            double score=getScore(item,state);
            if(score<0) continue;
            //选出最好分数的工具
            if(score>bestScore){
                bestScore=score;
                slot=i;
            }
        }
        //确定已经选择好了工具就切换
        if((slot!=-1&&bestScore>getScore(currentItem,state)&&!lowDurability(currentItem))||!isTools(currentItem)) {
            //记住上一次的槽方便恢复
            lastSlot=mc.player.getInventory().selectedSlot;
            //切换过去
            mc.player.getInventory().selectedSlot = slot;
            if(mc.interactionManager!=null) {
                ((ClientPlayerInteractionManagerAccessor) mc.interactionManager).syncSelectedSlot();
            }
        }
    }
    public double getScore(ItemStack item, BlockState state){
        double score=0;
        //Is Tool!!
        if(item.getItem() instanceof ToolItem || item.getItem() instanceof ShearsItem){
            //根据挖掘速度提升评分
            score+=item.getMiningSpeedMultiplier(state)*10;
            //附魔加分
            //耐久
            score+= EnchantmentHelper.getLevel(Enchantments.UNBREAKING,item)*10;
            //效率
            score+=EnchantmentHelper.getLevel(Enchantments.EFFICIENCY,item)*5;
            //经验修补(此项最优先)
            score+=EnchantmentHelper.getLevel(Enchantments.MENDING,item)*20;
            if (item.getItem() instanceof SwordItem item1 && (state.getBlock() instanceof BambooBlock || state.getBlock() instanceof BambooSaplingBlock))
                //根据挖掘等级加分
                score += 90 + (item1.getMaterial().getMiningLevel() * 10);
        }
        return score;
    }
    //停用低耐久度
    private boolean lowDurability(ItemStack itemStack) {
        return  (itemStack.getMaxDamage() - itemStack.getDamage()) < (itemStack.getMaxDamage() * 10 / 100);
    }
    public boolean isTools(ItemStack item){
        return item.getItem() instanceof ToolItem || item.getItem() instanceof ShearsItem;
    }
}
