package com.expvintl.mctools;

import com.expvintl.mctools.commands.*;
import com.expvintl.mctools.modules.BetterTooltip;
import com.expvintl.mctools.modules.CameraZoom;
import com.expvintl.mctools.texthud.MCInfo;
import com.expvintl.mctools.texthud.PotionInfo;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.util.Identifier;


public class MCToolsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //初始化命令注册回调
        ClientCommandRegistrationCallback.EVENT.register(MCToolsClient::registerCommands);
        HudElementRegistry.attachElementBefore(VanillaHudElements.CROSSHAIR, Identifier.of("mctools","DrawInfo"),MCInfo::drawHUD);
        HudElementRegistry.attachElementBefore(VanillaHudElements.CROSSHAIR, Identifier.of("mctools","DrawPotionInfo"),PotionInfo::drawHUD);

        InitModules();
    }
    public void InitModules(){
        BetterTooltip.INSTANCE.init();
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
