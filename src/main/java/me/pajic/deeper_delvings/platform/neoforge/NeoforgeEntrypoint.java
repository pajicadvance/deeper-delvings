package me.pajic.deeper_delvings.platform.neoforge;

//? neoforge {

/*import me.pajic.deeper_delvings.DeeperDelvings;
import me.pajic.deeper_delvings.block.ModBlocks;
import me.pajic.deeper_delvings.entity.ModEntities;
import me.pajic.deeper_delvings.item.ModCreativeTabs;
import me.pajic.deeper_delvings.item.ModItems;
import me.pajic.deeper_delvings.pack.ModDataPacks;
import me.pajic.deeper_delvings.worldgen.ModStructures;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.spider.CaveSpider;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(DeeperDelvings.MOD_ID)
@EventBusSubscriber(modid = DeeperDelvings.MOD_ID)
public class NeoforgeEntrypoint {

	@SubscribeEvent
	private static void onCommonSetup(FMLCommonSetupEvent event) {
        DeeperDelvings.onInitialize();
	}

	@SubscribeEvent
	private static void onRegister(RegisterEvent event) {
		event.register(Registries.BLOCK, helper -> ModBlocks.register(helper::register));
		event.register(Registries.ENTITY_TYPE, helper -> ModEntities.register(helper::register));
		event.register(Registries.ITEM, helper -> ModItems.register(helper::register));
		event.register(Registries.STRUCTURE_TYPE, helper -> ModStructures.register(helper::register));
		event.register(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, helper ->
				helper.register(DeeperDelvings.id("fiery_spider_spawns"), FierySpiderBiomeModifier.CODEC)
		);
	}

	@SubscribeEvent
	private static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
		event.put(ModEntities.FIERY_SPIDER, CaveSpider.createCaveSpider().build());
	}

	@SubscribeEvent
	private static void onRegisterSpawnPlacements(RegisterSpawnPlacementsEvent event) {
		event.register(
				ModEntities.FIERY_SPIDER,
				SpawnPlacementTypes.ON_GROUND,
				Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				Monster::checkMonsterSpawnRules,
				RegisterSpawnPlacementsEvent.Operation.REPLACE
		);
	}

	@SubscribeEvent
	private static void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
		ModCreativeTabs.getInsertions().forEach(insertion -> {
			if (event.getTabKey() != insertion.tab()) return;
			Item previous = insertion.after();
			for (Item item : insertion.items()) {
				event.insertAfter(previous.getDefaultInstance(), item.getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
				previous = item;
			}
		});
	}

	@SubscribeEvent
	private static void onAddPackFinders(AddPackFindersEvent event) {
		ModDataPacks.getPacks().forEach(pack -> event.addPackFinders(
				DeeperDelvings.id("resourcepacks/" + pack.id()),
				PackType.SERVER_DATA,
				Component.literal(pack.name()),
				PackSource.BUILT_IN,
				true,
				Pack.Position.TOP
		));
	}
}
*///?}
