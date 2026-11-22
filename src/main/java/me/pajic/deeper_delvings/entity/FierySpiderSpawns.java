package me.pajic.deeper_delvings.entity;

import me.pajic.deeper_delvings.DeeperDelvings;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.Weighted;
//~ if <26.2 'entity.EntityTypes;' -> 'entity.EntityType;'
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class FierySpiderSpawns {

    public static boolean isReplacementBiome(ResourceKey<Biome> biome) {
        return DeeperDelvings.CONFIG.modIntegration.fierySpiderBiomes.get().contains(biome.identifier());
    }

    public static boolean isSpider(Weighted<MobSpawnSettings.SpawnerData> spawn) {
        //~ if <26.2 'EntityTypes.SPIDER' -> 'EntityType.SPIDER'
        return spawn.value().type() == EntityTypes.SPIDER;
    }

    public static MobSpawnSettings.SpawnerData replace(MobSpawnSettings.SpawnerData spider) {
        //? if >=26.3 {
        return new MobSpawnSettings.SpawnerData(ModEntities.FIERY_SPIDER, spider.count());
        //?} else {
        /*return new MobSpawnSettings.SpawnerData(ModEntities.FIERY_SPIDER, spider.minCount(), spider.maxCount());
        *///?}
    }
}
