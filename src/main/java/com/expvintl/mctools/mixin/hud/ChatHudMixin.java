package com.expvintl.mctools.mixin.hud;

import com.expvintl.mctools.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    /* 1.21.6 起 mojang将这玩意塞到了一个函数里
    int p = this.forEachVisibleLine(i, currentTick, focused, m, (x1, y1, y2, line, messageIndex, backgroundOpacity) -> { <- method_71991
        int j = y2 + o;
        context.drawTextWithShadow(this.client.textRenderer, line.content(), x1, j, ColorHelper.withAlpha(backgroundOpacity * g, -1));
    });
    */
    @Inject(method = "method_71991",at= @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)V"))
    private void onRenderDrawTextWithShadow(int i, DrawContext draw, float f, int x1, int y1, int y2, ChatHudLine.Visible line, int messageIndex, float backgroundOpacity, CallbackInfo ci){
        double d = MinecraftClient.getInstance().options.getChatLineSpacing().getValue();
        int o = (int)Math.round(-8.0 * (d + 1.0) + 4.0 * d);
        int j = y2 + o; //计算消息实际高度
        Utils.DrawHeadIcon(draw,line, j);
        draw.getMatrices().pushMatrix();
        draw.getMatrices().translate(10, 0);
    }
    @Inject(method = "method_71991",at=@At(value = "TAIL"))
    private void method71991Tail(int i, DrawContext drawContext, float f, int x1, int y1, int y2, ChatHudLine.Visible line, int messageIndex, float backgroundOpacity, CallbackInfo ci){
        drawContext.getMatrices().popMatrix();
    }
}
