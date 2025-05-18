package com.expvintl.mctools.mixin.hud;

import com.expvintl.mctools.utils.Utils;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChatHud.class)
public class ChatHudMixin {
    @ModifyReceiver(method = "render",at=@At(value = "INVOKE",target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I"))
    private DrawContext onRenderDrawTextWithShadow(DrawContext context, TextRenderer renderer, OrderedText text, int x, int y, int color, @Local ChatHudLine.Visible line){
        Utils.DrawHeadIcon(context,line,y);
        return context;
    }
}
