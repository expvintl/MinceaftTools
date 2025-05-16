package com.expvintl.mctools.texthud;

import com.expvintl.mctools.utils.DrawUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.Colors;

import java.util.Collection;

public class PotionInfo {
    public static void drawHUD(DrawContext drawContext, float v) {
        MinecraftClient mc=MinecraftClient.getInstance();
        //跳过调试
        if(mc.options.debugEnabled||mc.options.hudHidden) return;

        if(mc.world!=null&&mc.player!=null) {
            DrawUtils.rightBottomY=1;
            Collection<StatusEffectInstance> effects=mc.player.getStatusEffects();
            for(StatusEffectInstance instance:effects){
                DrawUtils.AddRightBottomText(drawContext,String.format("%s%d (%s)", I18n.translate(instance.getTranslationKey()),
                        instance.getAmplifier()+1,
                        instance.isInfinite()?"无限":formatPotionDuration(instance.getDuration())));
            }
        }
    }
    public static String formatPotionDuration(int ticks) {
        int totalSeconds = ticks / 20; // 将ticks转换为秒
        int hours = totalSeconds / 3600; // 1小时 = 3600秒
        int minutes = (totalSeconds % 3600) / 60; // 获取剩余的分钟
        int seconds = totalSeconds % 60; // 获取剩余的秒数

        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
}
