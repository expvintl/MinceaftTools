package com.expvintl.mctools.texthud;

import com.expvintl.mctools.utils.DrawUtils;
import com.expvintl.mctools.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Colors;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public class MCInfo {
    private static String gameDayToRealTimeFormat(long gameDays) {
        // 游戏 1 小时等于 20 分钟
        long totalMinutes = gameDays * 20;

        long days = totalMinutes / (60 * 24); // 计算天数
        long remainingMinutesAfterDays = totalMinutes % (60 * 24);

        long hours = remainingMinutesAfterDays / 60; // 计算小时数
        long minutes = remainingMinutesAfterDays % 60; // 计算剩余分钟数

        StringBuilder timeString = new StringBuilder();

        if (days > 0) {
            timeString.append(days).append(" 天");
        }
        if (hours > 0) {
            if (!timeString.isEmpty()) {
                timeString.append(" ");
            }
            timeString.append(hours).append(" 小时");
        }
        if (minutes > 0 || timeString.isEmpty()) {
            if (!timeString.isEmpty()) {
                timeString.append(" ");
            }
            timeString.append(minutes).append(" 分钟");
        }

        return timeString.toString();
    }
    public static void drawHUD(DrawContext drawContext, RenderTickCounter v) {
        MinecraftClient mc=MinecraftClient.getInstance();
        //跳过调试
        if(mc.getDebugHud().shouldShowDebugHud()||mc.options.hudHidden) return;
        if(mc.world!=null&&mc.player!=null) {
            DrawUtils.leftTextY =1;
            int selfPing=0;
            ClientPlayerEntity p=mc.player;
            if(mc.getNetworkHandler()!=null&&mc.getNetworkHandler().getPlayerListEntry(mc.player.getGameProfile().getId())!=null) {
                selfPing=mc.getNetworkHandler().getPlayerListEntry(mc.player.getGameProfile().getId()).getLatency();
            }
            Vec3d playerPos=p.getPos();
            DrawUtils.AddLeftText(drawContext,String.format("%d FPS",mc.getCurrentFps()));
            DrawUtils.AddLeftText(drawContext,String.format("Ping: %d 毫秒",selfPing));
            DrawUtils.AddLeftText(drawContext,String.format("亮度:%d",mc.world.getLightLevel(p.getBlockPos())));
            DrawUtils.AddLeftText(drawContext,String.format("当前维度:%s", Utils.getCurrentDimensionName()));
            DrawUtils.AddLeftText(drawContext,String.format("当前群系:%s",Utils.getCurrentBiomeName()));
            if(Utils.getCurrentDimensionName().equals("下界")){
                DrawUtils.AddLeftText(drawContext,String.format("X:%.2f Y:%.2f Z:%.2f (主世界 X:%.2f Z:%.2f)",playerPos.x,playerPos.y,playerPos.z,playerPos.x*8,playerPos.z*8));
            }else if(Utils.getCurrentDimensionName().equals("主世界")){
                DrawUtils.AddLeftText(drawContext,String.format("X:%.2f Y:%.2f Z:%.2f (下界 X:%.2f Z:%.2f)",playerPos.x,playerPos.y,playerPos.z,playerPos.x/8,playerPos.z/8));
            }else{
                DrawUtils.AddLeftText(drawContext,String.format("X:%.2f Y:%.2f Z:%.2f",playerPos.x,playerPos.y,playerPos.z));
            }
            DrawUtils.AddLeftText(drawContext,String.format("世界时间: %d天 (%s)",mc.world.getTimeOfDay()/24000,gameDayToRealTimeFormat(mc.world.getTimeOfDay()/24000)));
            DrawUtils.AddLeftText(drawContext,String.format("当前区块: [%d,%d]",mc.player.getChunkPos().x,mc.player.getChunkPos().z));
            DrawUtils.AddLeftText(drawContext,String.format("本地难度:%.2f",mc.world.getLocalDifficulty(mc.player.getBlockPos()).getLocalDifficulty()));
            ItemStack currentItem=p.getMainHandStack();
            if(Objects.nonNull(currentItem)&&currentItem.isDamageable()){
                DrawUtils.AddLeftText(drawContext,String.format("耐久度:%d/%d",currentItem.getMaxDamage()-currentItem.getDamage(),currentItem.getMaxDamage()));
            }
        }
    }
}
