package com.expvintl.mctools;

import com.expvintl.mctools.commands.*;
import com.expvintl.mctools.modules.BetterTooltip;
import com.expvintl.mctools.modules.CameraZoom;
import com.expvintl.mctools.modules.PlayerListTextLatency;
import com.expvintl.mctools.texthud.MCInfo;
import com.expvintl.mctools.texthud.PotionInfo;
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
    @Override
    public void onInitializeClient() {
        //初始化命令注册回调
        ClientCommandRegistrationCallback.EVENT.register(MCToolsClient::registerCommands);
        HudRenderCallback.EVENT.register(MCInfo::drawHUD);
        HudRenderCallback.EVENT.register(PotionInfo::drawHUD);
        InitModules();
    }
    public void InitModules(){
        BetterTooltip.INSTANCE.init();
        PlayerListTextLatency.INSTANCE.init();
        CameraZoom.INSTANCE.init();
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
        CFastDropCommand.register(dispatcher,registryAccess);
    }
}
