package me.pajic.deeper_delvings.block;

import me.pajic.deeper_delvings.DeeperDelvings;
import me.pajic.deeper_delvings.mixin.block.StairBlockAccessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ModBlocks {

    private static final Map<ResourceKey<Block>, Block> BLOCKS = new LinkedHashMap<>();

    public static final Block MOSSY_COBBLED_DEEPSLATE = create(
            "mossy_cobbled_deepslate", Block::new, Blocks.COBBLED_DEEPSLATE
    );
    public static final Block MOSSY_COBBLED_DEEPSLATE_STAIRS = create(
            "mossy_cobbled_deepslate_stairs",
            properties -> StairBlockAccessor.ac$create(MOSSY_COBBLED_DEEPSLATE.defaultBlockState(), properties),
            Blocks.COBBLED_DEEPSLATE_STAIRS
    );
    public static final Block MOSSY_COBBLED_DEEPSLATE_SLAB = create(
            "mossy_cobbled_deepslate_slab", SlabBlock::new, Blocks.COBBLED_DEEPSLATE_SLAB
    );
    public static final Block MOSSY_COBBLED_DEEPSLATE_WALL = create(
            "mossy_cobbled_deepslate_wall", WallBlock::new, Blocks.COBBLED_DEEPSLATE_WALL
    );
    public static final Block MOSSY_DEEPSLATE_BRICKS = create(
            "mossy_deepslate_bricks", Block::new, Blocks.DEEPSLATE_BRICKS
    );
    public static final Block MOSSY_DEEPSLATE_BRICK_STAIRS = create(
            "mossy_deepslate_brick_stairs",
            properties -> StairBlockAccessor.ac$create(MOSSY_DEEPSLATE_BRICKS.defaultBlockState(), properties),
            Blocks.DEEPSLATE_BRICK_STAIRS
    );
    public static final Block MOSSY_DEEPSLATE_BRICK_SLAB = create(
            "mossy_deepslate_brick_slab", SlabBlock::new, Blocks.DEEPSLATE_BRICK_SLAB
    );
    public static final Block MOSSY_DEEPSLATE_BRICK_WALL = create(
            "mossy_deepslate_brick_wall", WallBlock::new, Blocks.DEEPSLATE_BRICK_WALL
    );
    public static final Block INFESTED_MOSSY_DEEPSLATE_BRICKS = create(
            "infested_mossy_deepslate_bricks",
            properties -> new InfestedBlock(MOSSY_DEEPSLATE_BRICKS, properties),
            Blocks.INFESTED_DEEPSLATE
    );

    public static void register(BiConsumer<ResourceKey<Block>, Block> registrar) {
        BLOCKS.forEach(registrar);
    }

    public static Map<ResourceKey<Block>, Block> getBlocks() {
        return BLOCKS;
    }

    private static Block create(String name, Function<BlockBehaviour.Properties, Block> factory, Block copyFrom) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, DeeperDelvings.id(name));
        Block block = factory.apply(BlockBehaviour.Properties.ofFullCopy(copyFrom).setId(key));
        BLOCKS.put(key, block);
        return block;
    }
}
