package com.expvintl.mctools.events.client;

import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;

public class RenderWorldEvent {
    private static final RenderWorldEvent INSTANCE=new RenderWorldEvent();
    public MatrixStack matrices;
    public float tickDelta;
    public Camera cam;

    public static RenderWorldEvent get(float tickDelta,Camera cam,MatrixStack matrices){
        INSTANCE.tickDelta=tickDelta;
        INSTANCE.cam=cam;
        INSTANCE.matrices=matrices;
        return INSTANCE;
    }

}
