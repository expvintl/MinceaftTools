package com.expvintl.mctools.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Colors;

public class DrawUtils {
    public static int leftTextY=1;
    public static int rightTextY=1;
    public static int rightBottomY=1;
    public static void AddLeftText(DrawContext drawContext, String text){
        MinecraftClient mc=MinecraftClient.getInstance();
            drawContext.drawText(mc.textRenderer,text,0,leftTextY, Colors.WHITE,false);
            leftTextY+=10;
    }
    public static void AddRightText(DrawContext drawContext, String text){
        MinecraftClient mc=MinecraftClient.getInstance();
        drawContext.drawText(mc.textRenderer,text,drawContext.getScaledWindowWidth()-2-mc.textRenderer.getWidth(text),rightTextY, Colors.WHITE,false);
        rightTextY+=10;
    }
    public static void AddRightBottomText(DrawContext drawContext, String text){
        MinecraftClient mc=MinecraftClient.getInstance();
        drawContext.drawText(mc.textRenderer,text,drawContext.getScaledWindowWidth()-2-mc.textRenderer.getWidth(text),drawContext.getScaledWindowHeight()-10-rightBottomY, Colors.WHITE,false);
        rightBottomY+=10;
    }
}
