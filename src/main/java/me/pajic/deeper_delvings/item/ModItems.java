package me.pajic.deeper_delvings.item;

import me.pajic.deeper_delvings.DeeperDelvings;
import me.pajic.deeper_delvings.ModFeatures;
import me.pajic.deeper_delvings.block.ModBlocks;
import me.pajic.deeper_delvings.entity.ModEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ModItems {

    private static final Map<ResourceKey<Item>, Item> ITEMS = new LinkedHashMap<>();
    private static final Map<Block, Item> BLOCK_ITEMS = new LinkedHashMap<>();

    static {
        boolean mossyDeepslate = ModFeatures.mossyDeepslateSet();
        ModBlocks.getBlocks().forEach((blockKey, block) -> {
            ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, blockKey.identifier());
            Item item = new ModBlockItem(block, new Item.Properties().setId(key).useBlockDescriptionPrefix(), mossyDeepslate);
            ITEMS.put(key, item);
            BLOCK_ITEMS.put(block, item);
        });
    }

    public static final Item FIERY_SPIDER_SPAWN_EGG = createSpawnEgg(
            "fiery_spider_spawn_egg", ModEntities.FIERY_SPIDER, ModFeatures.fierySpider()
    );

    public static void register(BiConsumer<ResourceKey<Item>, Item> registrar) {
        ITEMS.forEach(registrar);
    }

    public static Item blockItem(Block block) {
        return BLOCK_ITEMS.get(block);
    }

    private static Item createSpawnEgg(String name, EntityType<?> type, boolean enabled) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, DeeperDelvings.id(name));
        Item item = new ModSpawnEggItem(new Item.Properties().setId(key).spawnEgg(type), enabled);
        ITEMS.put(key, item);
        return item;
    }
}
