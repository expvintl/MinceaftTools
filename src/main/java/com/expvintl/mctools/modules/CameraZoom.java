package com.expvintl.mctools.modules;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class CameraZoom {
    public static CameraZoom INSTANCE=new CameraZoom();
    public boolean isZoom=false;
    public void init(){
        KeyBinding zoom= KeyBindingHelper.registerKeyBinding(new KeyBinding("镜头放大", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C,"MyTools"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            isZoom=zoom.isPressed();
        });
    }

}
