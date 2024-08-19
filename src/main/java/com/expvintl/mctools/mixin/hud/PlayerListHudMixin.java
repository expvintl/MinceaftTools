package com.expvintl.mctools.mixin.hud;

import com.expvintl.mctools.events.MCEventBus;
import com.expvintl.mctools.events.hud.RenderLatencyIconEvent;
import com.expvintl.mctools.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
    @ModifyArg(method = "render",at=@At(value = "INVOKE",target = "Ljava/lang/Math;min(II)I"),index = 0)
    private int fixWidth(int width){
        return width+25;
    }
    @Inject(method = "getPlayerName",at=@At("HEAD"),cancellable = true)
    private void getPlayerName(PlayerListEntry entry, CallbackInfoReturnable<Text> info){
        if(Utils.isReady()){
            Text name=entry.getDisplayName();
            if(entry.getProfile().getId().toString().equals(MinecraftClient.getInstance().player.getGameProfile().getId().toString())){
                info.setReturnValue(Text.literal(name.getString()).setStyle(name.getStyle().withColor(0xff0000)));
            }
        }
    }

    @Inject(method = "renderLatencyIcon",at=@At("HEAD"),cancellable = true)
    private void onRenderLatencyIcon(DrawContext draw, int width, int x, int y, PlayerListEntry entry, CallbackInfo info){
        MCEventBus.INSTANCE.post(RenderLatencyIconEvent.get(draw,width,x,y,entry,info));
    }
}
