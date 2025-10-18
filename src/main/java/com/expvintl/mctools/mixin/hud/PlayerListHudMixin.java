package com.expvintl.mctools.mixin.hud;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
    @Unique
    private int calcLatencyColor(int latency){
        if(latency>=0&&latency<=60){ //0-60
            return ColorHelper.withAlpha(255,0x00FF00); //绿色
        }else if(latency>60&&latency<=100){ //60-100
            return ColorHelper.withAlpha(255,0xFFFF00); //黄色
        }else if(latency>100&&latency<=200){//100-200
            return ColorHelper.withAlpha(255,0xFFA500); //橙色
        }else if(latency>200){ //>200
            return ColorHelper.withAlpha(255,0xFF0000); //红色
        }
        return ColorHelper.withAlpha(255,0xFFFFFF); //默认白色
    }
    @ModifyArg(method = "render",at=@At(value = "INVOKE",target = "Ljava/lang/Math;min(II)I"),index = 0)
    private int fixWidth(int width){
        return width+25;
    }
    @Inject(method = "getPlayerName",at=@At("HEAD"),cancellable = true)
    private void getPlayerName(PlayerListEntry entry, CallbackInfoReturnable<Text> info) {
        Text name = entry.getDisplayName();
        if (MinecraftClient.getInstance().player == null || name == null) return;
        if (entry.getProfile().id().toString().equals(MinecraftClient.getInstance().player.getGameProfile().id().toString())) {
            info.setReturnValue(Text.literal(name.getString()).setStyle(name.getStyle().withColor(0xff0000)));
        }
    }
    @Inject(method = "renderLatencyIcon",at=@At("HEAD"),cancellable = true)
    private void onRenderLatencyIcon(DrawContext draw, int width, int x, int y, PlayerListEntry entry, CallbackInfo info){
        TextRenderer renderer=MinecraftClient.getInstance().textRenderer;
        int latency=Math.clamp(entry.getLatency(),0,999);
        String text=latency+" ms";
        draw.drawTextWithShadow(renderer,text,x+width-renderer.getWidth(text),y,calcLatencyColor(latency));
        info.cancel();
    }

    //强制启用Tab列表玩家头像
    @ModifyVariable(method = "render",at = @At(value = "STORE",ordinal = 0),ordinal = 0)
    private boolean hackShowPlayerHeadIcon(boolean b1){
        return true;
    }
}
