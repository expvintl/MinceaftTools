package com.expvintl.mctools;

import com.expvintl.mctools.settingtype.BooleanSetting;

public class FeaturesSettings {
    public final static FeaturesSettings INSTANCE=new FeaturesSettings();
    public final BooleanSetting autoRespawn = new BooleanSetting();
    public final BooleanSetting safeWalk = new BooleanSetting();
    public final BooleanSetting checkBukkitPlugins = new BooleanSetting();
    public final BooleanSetting autoTool = new BooleanSetting();
    public final BooleanSetting autoToolIncludePlayer = new BooleanSetting();
    public final BooleanSetting autoFish = new BooleanSetting();
    public final BooleanSetting noFallPacket = new BooleanSetting();
}
