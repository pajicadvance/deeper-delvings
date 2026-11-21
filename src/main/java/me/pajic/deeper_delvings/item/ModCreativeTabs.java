package me.pajic.deeper_delvings.item;

import me.pajic.deeper_delvings.DeeperDelvings;
import me.pajic.deeper_delvings.block.ModBlocks;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModCreativeTabs {

    public static List<Insertion> getInsertions() {
        List<Insertion> insertions = new ArrayList<>();
        if (DeeperDelvings.CONFIG.content.mossyDeepslateSet.get()) {
            insertions.add(new Insertion(
                    CreativeModeTabs.BUILDING_BLOCKS, Items.COBBLED_DEEPSLATE_WALL, blockItems(
                            ModBlocks.MOSSY_COBBLED_DEEPSLATE, ModBlocks.MOSSY_COBBLED_DEEPSLATE_STAIRS,
                            ModBlocks.MOSSY_COBBLED_DEEPSLATE_SLAB, ModBlocks.MOSSY_COBBLED_DEEPSLATE_WALL
                    )
            ));
            insertions.add(new Insertion(
                    CreativeModeTabs.BUILDING_BLOCKS, Items.DEEPSLATE_BRICK_WALL, blockItems(
                            ModBlocks.MOSSY_DEEPSLATE_BRICKS, ModBlocks.MOSSY_DEEPSLATE_BRICK_STAIRS,
                            ModBlocks.MOSSY_DEEPSLATE_BRICK_SLAB, ModBlocks.MOSSY_DEEPSLATE_BRICK_WALL
                    )
            ));
            insertions.add(new Insertion(
                    CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.INFESTED_DEEPSLATE, blockItems(ModBlocks.INFESTED_MOSSY_DEEPSLATE_BRICKS)
            ));
        }
        if (DeeperDelvings.CONFIG.content.fierySpider.get()) {
            insertions.add(new Insertion(
                    CreativeModeTabs.SPAWN_EGGS, Items.CAVE_SPIDER_SPAWN_EGG, List.of(ModItems.FIERY_SPIDER_SPAWN_EGG)
            ));
        }
        return insertions;
    }

    private static List<Item> blockItems(Block... blocks) {
        return Arrays.stream(blocks).map(ModItems::blockItem).toList();
    }

    public record Insertion(ResourceKey<CreativeModeTab> tab, Item after, List<Item> items) {}
}
