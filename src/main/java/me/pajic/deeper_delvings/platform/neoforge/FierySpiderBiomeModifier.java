package me.pajic.deeper_delvings.platform.neoforge;

//? neoforge {

/*import com.mojang.serialization.MapCodec;
import me.pajic.deeper_delvings.entity.FierySpiderSpawns;
import net.minecraft.core.Holder;
import net.minecraft.util.random.Weighted;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.MobSpawnSettingsBuilder;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
//? if >=26.3 {
import net.minecraft.core.RegistryAccess;
//?}

public class FierySpiderBiomeModifier implements BiomeModifier {

	public static final FierySpiderBiomeModifier INSTANCE = new FierySpiderBiomeModifier();
	public static final MapCodec<FierySpiderBiomeModifier> CODEC = MapCodec.unit(INSTANCE);

	@Override
	//? if >=26.3 {
	public void modify(@NotNull RegistryAccess registryAccess, @NotNull Holder<Biome> biome, @NotNull Phase phase, @NotNull ModifiableBiomeInfo.BiomeInfo.Builder builder) {
	//?} else {
	/^public void modify(@NotNull Holder<Biome> biome, @NotNull Phase phase, @NotNull ModifiableBiomeInfo.BiomeInfo.Builder builder) {
	^///?}
		if (phase != Phase.REMOVE || biome.unwrapKey().filter(FierySpiderSpawns::isReplacementBiome).isEmpty()) return;
		MobSpawnSettingsBuilder spawns = builder.getMobSpawnSettings();
		for (MobCategory category : MobCategory.values()) {
			List<Weighted<SpawnerData>> spiders = new ArrayList<>();
			spawns.getSpawner(category).removeIf(spawn -> {
				if (!FierySpiderSpawns.isSpider(spawn)) return false;
				spiders.add(spawn);
				return true;
			});
			spiders.forEach(spider -> spawns.getSpawner(category).add(FierySpiderSpawns.replace(spider.value()), spider.weight()));
		}
	}

	@Override
	public @NotNull MapCodec<? extends BiomeModifier> codec() {
		return CODEC;
	}
}
*///?}
