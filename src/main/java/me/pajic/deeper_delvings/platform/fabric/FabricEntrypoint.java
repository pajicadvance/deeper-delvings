package me.pajic.deeper_delvings.platform.fabric;

//? fabric {

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import me.pajic.deeper_delvings.DeeperDelvings;
import me.pajic.deeper_delvings.block.ModBlocks;
import me.pajic.deeper_delvings.entity.FierySpiderSpawns;
import me.pajic.deeper_delvings.entity.ModEntities;
import me.pajic.deeper_delvings.item.ModCreativeTabs;
import me.pajic.deeper_delvings.item.ModItems;
import me.pajic.deeper_delvings.mixin.entity.SpawnPlacementsInvoker;
import me.pajic.deeper_delvings.pack.ModDataPacks;
import me.pajic.deeper_delvings.worldgen.ModStructures;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.random.Weighted;
//~ if <26.2 'entity.EntityTypes;' -> 'entity.EntityType;'
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.spider.CaveSpider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.List;

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		ModBlocks.register((key, block) -> Registry.register(BuiltInRegistries.BLOCK, key, block));
		ModEntities.register((key, type) -> Registry.register(BuiltInRegistries.ENTITY_TYPE, key, type));
		ModItems.register((key, item) -> Registry.register(BuiltInRegistries.ITEM, key, item));
		ModStructures.register((key, type) -> Registry.register(BuiltInRegistries.STRUCTURE_TYPE, key, type));

		FabricDefaultAttributeRegistry.register(ModEntities.FIERY_SPIDER, CaveSpider.createCaveSpider());
		SpawnPlacementsInvoker.dd$callRegister(
				ModEntities.FIERY_SPIDER,
				SpawnPlacementTypes.ON_GROUND,
				Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				Monster::checkMonsterSpawnRules
		);

		ModCreativeTabs.getInsertions().forEach(insertion ->
				CreativeModeTabEvents.modifyOutputEvent(insertion.tab()).register(output ->
						output.insertAfter(insertion.after(), insertion.items().toArray(Item[]::new))
				)
		);

		BiomeModifications.create(DeeperDelvings.id("fiery_spider_spawns")).add(
				ModificationPhase.REPLACEMENTS,
				context -> FierySpiderSpawns.isReplacementBiome(context.getBiomeKey()),
				context -> {
					BiomeModificationContext.MobSpawnSettingsContext spawns = context.getMobSpawnSettings();
					List<Weighted<MobSpawnSettings.SpawnerData>> spiders = spawns.getMobs(MobCategory.MONSTER).stream()
							.filter(FierySpiderSpawns::isSpider)
							.toList();
					//~ if <26.2 'EntityTypes.SPIDER' -> 'EntityType.SPIDER'
					spawns.removeSpawnsOfEntityType(EntityTypes.SPIDER);
					//? if >=26.3 {
					spiders.forEach(spider -> spawns.addSpawn(ModEntities.FIERY_SPIDER, spider.weight(), spider.value().count()));
					//?} else {
					/*spiders.forEach(spider -> spawns.addSpawn(MobCategory.MONSTER, FierySpiderSpawns.replace(spider.value()), spider.weight()));
					*///?}
				}
		);

		ModContainer container = FabricLoader.getInstance().getModContainer(DeeperDelvings.MOD_ID).orElseThrow();
		ModDataPacks.getPacks().forEach(pack -> ResourceLoader.registerBuiltinPack(
				DeeperDelvings.id(pack.id()),
				container,
				Component.literal(pack.name()),
				PackActivationType.ALWAYS_ENABLED
		));

		DeeperDelvings.onInitialize();
	}
}
//?}
