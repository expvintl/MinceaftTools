package com.expvintl.mctools.utils;

import com.expvintl.mctools.mixin.interfaces.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

import java.util.Objects;
import java.util.Optional;
import java.util.Timer;

public class Utils {
    private static final MinecraftClient mc=MinecraftClient.getInstance();
    public static final Timer timer=new Timer();
    public static String getCurrentDimensionName(){
        if(mc.world!=null){
            String dismenName=mc.world.getDimensionKey().getValue().toString();
            switch (dismenName){
                case "minecraft:overworld":
                    return "主世界";
                case "minecraft:the_nether":
                    return "下界";
                case "minecraft:the_end":
                    return "末地";
                default: return dismenName;
            }
        }
        return "未知";
    }
    public static String getCurrentBiomeName(){
        if(Objects.nonNull(mc.world)&&Objects.nonNull(mc.player)){
            Optional<RegistryKey<Biome>> biomeName=mc.world.getBiome(mc.player.getBlockPos()).getKey();
            if(biomeName.isPresent()){
                String name=biomeName.get().getValue().toString();
                switch (name){
                    case "minecraft:badlands": return "恶地 (badlands)";
                    case "minecraft:bamboo_jungle": return "竹林 (bamboo_jungle)";
                    case "minecraft:basalt_deltas": return "玄武岩三角洲 (basalt_deltas)";
                    case "minecraft:beach":return "沙滩 (beach)";
                    case "minecraft:ocean": return "海洋 (ocean)";
                    case "minecraft:plains": return "平原 (plains)";
                    case "minecraft:river": return "河流 (river)";
                    case "minecraft:birch_forest":return "桦木森林 (birch_forest)";
                    case "minecraft:cherry_grove":return "樱花树林 (cherry_grove)";
                    case "minecraft:cold_ocean": return "冷水海洋 (cold_ocean)";
                    case "minecraft:crimson_forest": return "绯红森林 (crimson_forest)";
                    case "minecraft:dark_forest": return "黑森林 (dark_forest)";
                    case "minecraft:deep_cold_ocean": return "冷水深海 (deep_cold_ocean)";
                    case "minecraft:deep_dark": return "深暗之域 (deep_dark)";
                    case "minecraft:deep_frozen_ocean": return "冰冻深海 (deep_frozen_ocean)";
                    case "minecraft:deep_lukewarm_ocean":return "温水深海 (deep_lukewarm_ocean)";
                    case "minecraft:deep_ocean":return "深海 (deep_ocean)";
                    case "minecraft:desert":return "沙漠 (desert)";
                    case "minecraft:dripstone_caves":return "溶洞 (dripstone_caves)";
                    case "minecraft:end_barrens": return "末地荒地 (end_barrens)";
                    case "minecraft:end_highlands": return "末地高地 (end_highlands)";
                    case "minecraft:eroded_badlands": return "风蚀恶地 (eroded_badlands)";
                    case "minecraft:flower_forest":return "繁花森林 (flower_forest)";
                    case "minecraft:forest":return "森林 (forest)";
                    case "minecraft:frozen_ocean":return "冻洋 (frozen_ocean)";
                    case "minecraft:frozen_peaks":return "冰封山峰 (frozen_peaks)";
                    case "minecraft:frozen_river":return "冻河 (frozen_river)";
                    case "minecraft:grove": return "雪林 (grove)";
                    case "minecraft:ice_spikes": return "冰刺之地 (ice_spikes)";
                    case "minecraft:jagged_peaks": return "尖峭山峰 (jagged_peaks)";
                    case "minecraft:jungle":return "丛林 (jungle)";
                    case "minecraft:lukewarm_ocean":return "温水海洋 (lukewarm_ocean)";
                    case "minecraft:lush_caves":return "繁茂洞穴 (lush_caves)";
                    case "minecraft:mangrove_swamp":return "红树林沼泽 (mangrove_swamp)";
                    case "minecraft:meadow":return "草甸 (meadow)";
                    case "minecraft:mushroom_fields": return "蘑菇岛 (mushroom_fields)";
                    case "minecraft:nether_wastes": return "下界荒地 (nether_wastes)";
                    case "minecraft:old_growth_birch_forest":return "原始桦木森林 (old_growth_birch_forest)";
                    case "minecraft:old_growth_pine_taiga":return "原始松木针叶林 (old_growth_pine_taiga)";
                    case "minecraft:old_growth_spruce_taiga":return "原始云杉针叶林 (old_growth_spruce_taiga)";
                    case "minecraft:savanna":return "热带草原 (savanna)";
                    case "minecraft:savanna_plateau": return "热带高原 (savanna_plateau)";
                    case "minecraft:small_end_islands":return "末地小型岛屿 (small_end_islands)";
                    case "minecraft:snowy_beach":return "积雪沙滩 (snowy_beach)";
                    case "minecraft:snowy_plains":return "雪原 (snowy_plains)";
                    case "minecraft:snowy_slopes": return "积雪山坡 (snowy_slopes)";
                    case "minecraft:snowy_taiga":return "积雪针叶林 (snowy_taiga)";
                    case "minecraft:soul_sand_valley":return "灵魂沙峡谷 (soul_sand_valley)";
                    case "minecraft:sparse_jungle": return "稀疏丛林 (sparse_jungle)";
                    case "minecraft:stony_peaks": return "裸岩山峰 (stony_peaks)";
                    case "minecraft:stony_shore": return "石岸 (stony_shore)";
                    case "minecraft:sunflower_plains": return "向日葵平原 (sunflower_plains)";
                    case "minecraft:swamp":return "沼泽 (swamp)";
                    case "minecraft:taiga":return "针叶林 (taiga)";
                    case "minecraft:the_end":return "末地 (the_end)";
                    case "minecraft:the_void": return "虚空 (the_void)";
                    case "minecraft:warm_ocean":return "暖水海洋 (warm_ocean)";
                    case "minecraft:warped_forest":return "诡异森林 (warped_forest)";
                    case "minecraft:windswept_forest":return "风袭森林 (windswept_forest)";
                    case "minecraft:windswept_gravelly_hills":return "风袭沙砾丘陵 (windswept_gravelly_hills)";
                    case "minecraft:windswept_hills":return "风袭丘陵 (windswept_hills)";
                    case "minecraft:windswept_savanna":return "风袭热带草原 (windswept_savanna)";
                    case "minecraft:wooded_badlands":return "疏林恶地 (wooded_badlands)";
                    default:return name;
                }
            }
        }
        return "未知";
    }
    public static void rightClick(){
        ((MinecraftClientAccessor)mc).doItemUse();
    }
}
