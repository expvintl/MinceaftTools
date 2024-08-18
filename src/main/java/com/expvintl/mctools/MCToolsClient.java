package com.expvintl.mctools;

import com.expvintl.mctools.commands.*;
import com.expvintl.mctools.utils.Utils;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Colors;
import net.minecraft.util.math.Vec3d;


public class MCToolsClient implements ClientModInitializer {
    private static int infoY=1;
    @Override
    public void onInitializeClient() {

        //初始化命令注册回调
        ClientCommandRegistrationCallback.EVENT.register(MCToolsClient::registerCommands);
        HudRenderCallback.EVENT.register(MCToolsClient::drawHUD);

    }
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
            if (timeString.length() > 0) {
                timeString.append(" ");
            }
            timeString.append(hours).append(" 小时");
        }
        if (minutes > 0 || timeString.length() == 0) {
            if (timeString.length() > 0) {
                timeString.append(" ");
            }
            timeString.append(minutes).append(" 分钟");
        }

        return timeString.toString();
    }
    private static void drawHUD(DrawContext drawContext, RenderTickCounter v) {
        MinecraftClient mc=MinecraftClient.getInstance();
        //跳过调试
        if(mc.options.hudHidden) return;

        if(mc.world!=null&&mc.player!=null) {
            infoY=1;
            int selfPing=0;
            ClientPlayerEntity p=mc.player;
            if(mc.getNetworkHandler()!=null&&mc.getNetworkHandler().getPlayerListEntry(mc.player.getGameProfile().getId())!=null) {
                selfPing=mc.getNetworkHandler().getPlayerListEntry(mc.player.getGameProfile().getId()).getLatency();
            }
            Vec3d playerPos=p.getPos();
            AddText(drawContext,String.format("%d FPS",mc.getCurrentFps()));
            AddText(drawContext,String.format("Ping: %d 毫秒",selfPing));
            AddText(drawContext,String.format("亮度:%d",mc.world.getLightLevel(p.getBlockPos())));
            AddText(drawContext,String.format("当前维度:%s", Utils.getCurrentDimensionName()));
            AddText(drawContext,String.format("当前群系:%s",Utils.getCurrentBiomeName()));
            if(Utils.getCurrentDimensionName().equals("下界")){
                AddText(drawContext,String.format("X:%.2f Y:%.2f Z:%.2f (主世界 X:%.2f Z:%.2f)",playerPos.x,playerPos.y,playerPos.z,playerPos.x*8,playerPos.z*8));
            }else if(Utils.getCurrentDimensionName().equals("主世界")){
                AddText(drawContext,String.format("X:%.2f Y:%.2f Z:%.2f (下界 X:%.2f Z:%.2f)",playerPos.x,playerPos.y,playerPos.z,playerPos.x/8,playerPos.z/8));
            }else{
                AddText(drawContext,String.format("X:%.2f Y:%.2f Z:%.2f",playerPos.x,playerPos.y,playerPos.z));
            }
            AddText(drawContext,String.format("世界时间: %d天 (%s)",mc.world.getTimeOfDay()/24000,gameDayToRealTimeFormat(mc.world.getTimeOfDay()/24000)));
            AddText(drawContext,String.format("当前区块: [%d,%d]",mc.player.getChunkPos().x,mc.player.getChunkPos().z));
            AddText(drawContext,String.format("本地难度:%.2f",mc.world.getLocalDifficulty(mc.player.getBlockPos()).getLocalDifficulty()));
            AddText(drawContext,String.format("服务器视距:%d 区块",mc.options.getSyncedOptions().viewDistance()));

            ItemStack currentItem=p.getInventory().getMainHandStack();
            if(currentItem!=null&&currentItem.isDamageable()){
                AddText(drawContext,String.format("耐久度:%d/%d",currentItem.getMaxDamage()-currentItem.getDamage(),currentItem.getMaxDamage()));
            }
        }
    }
    private static void AddText(DrawContext drawContext,String text){
        TextRenderer renderer=MinecraftClient.getInstance().textRenderer;
        drawContext.drawText(renderer,text,0,infoY,Colors.WHITE,false);
        infoY+=10;
    }
    private static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        //注册命令
        CFullbirghtCommand.register(dispatcher);
        CAutoRespawnCommand.register(dispatcher);
        CSafeWalkCommand.register(dispatcher);
        CAutoFishCommand.register(dispatcher);
        CAutoToolCommand.register(dispatcher);
        CQServerPluginsCommand.register(dispatcher);
        CNoFallPacketCommand.register(dispatcher);
        CFindBlockCommand.register(dispatcher);
        CFastDropCommand.register(dispatcher);
    }
}
