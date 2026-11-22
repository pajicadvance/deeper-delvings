package me.pajic.deeper_delvings.worldgen.mineshaft;

import me.pajic.deeper_delvings.DeeperDelvings;
import me.pajic.deeper_delvings.mixin.worldgen.StructurePieceAccessor;
import me.pajic.deeper_delvings.util.StructureGenUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
//~ if <26.2 'entity.EntityTypes;' -> 'entity.EntityType;'
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.storage.loot.LootTable;

public class NetherMineshaftDecorator {

    private static final ResourceKey<LootTable> CRIMSON_MINESHAFT_LOOT = ResourceKey.create(Registries.LOOT_TABLE, DeeperDelvings.id("chests/crimson_mineshaft"));
    private static final ResourceKey<LootTable> WARPED_MINESHAFT_LOOT = ResourceKey.create(Registries.LOOT_TABLE, DeeperDelvings.id("chests/warped_mineshaft"));
    private static final float SPIDER_CORRIDOR_COBWEB_CHANCE = 0.075F;
    private static final float PIGLIN_MINER_CHANCE = 0.15F;
    private static final float ENDERMAN_CHANCE = PIGLIN_MINER_CHANCE;

    public static ResourceKey<LootTable> lootTable(StructurePiece piece, ResourceKey<LootTable> original) {
        if (StructureGenUtil.isCrimsonMineshaftPiece(piece)) return CRIMSON_MINESHAFT_LOOT;
        if (StructureGenUtil.isWarpedMineshaftPiece(piece)) return WARPED_MINESHAFT_LOOT;
        return original;
    }

    public static float spiderCorridorCobwebChance() {
        return SPIDER_CORRIDOR_COBWEB_CHANCE;
    }

    public static void placeFoliage(StructurePiece piece, WorldGenLevel level, BoundingBox chunkBB, RandomSource random, float chance, int x, int y, int z) {
        if (random.nextFloat() >= chance * 2) return;
        StructurePieceAccessor accessor = (StructurePieceAccessor) piece;
        if (StructureGenUtil.isCrimsonMineshaftPiece(piece)) {
            if (random.nextBoolean()) placeHangingVines(accessor, level, chunkBB, random, x, y, z);
            else placeRoots(accessor, level, chunkBB, x, z, Blocks.CRIMSON_ROOTS, Blocks.CRIMSON_NYLIUM);
        } else {
            int choice = random.nextInt(3);
            if (choice == 0) placeTwistingVines(accessor, level, chunkBB, random, x, z);
            else placeRoots(accessor, level, chunkBB, x, z, choice == 1 ? Blocks.WARPED_ROOTS : Blocks.NETHER_SPROUTS, Blocks.WARPED_NYLIUM);
        }
    }

    public static void decorateCorridor(StructurePiece piece, WorldGenLevel level, BoundingBox chunkBB, int numSections) {
        StructurePieceAccessor accessor = (StructurePieceAccessor) piece;
        long seed = accessor.dd$callGetWorldPos(0, 0, 0).asLong();
        int length = numSections * 5;
        placeAncientDebris(accessor, level, chunkBB, RandomSource.create(seed), length);
        if (StructureGenUtil.isCrimsonMineshaftPiece(piece) && RandomSource.create(seed ^ 0x5DEECE66DL).nextFloat() < PIGLIN_MINER_CHANCE) {
            spawnPiglinMiner(accessor, level, chunkBB, length);
        }
        if (StructureGenUtil.isWarpedMineshaftPiece(piece) && RandomSource.create(seed ^ 0x5DEECE66DL).nextFloat() < ENDERMAN_CHANCE) {
            spawnEnderman(accessor, level, chunkBB, length);
        }
    }

    private static void placeHangingVines(StructurePieceAccessor accessor, WorldGenLevel level, BoundingBox chunkBB, RandomSource random, int x, int y, int z) {
        BlockPos ceiling = accessor.dd$callGetWorldPos(x, y + 1, z);
        if (!chunkBB.isInside(accessor.dd$callGetWorldPos(x, y, z)) || !level.getBlockState(ceiling).isFaceSturdy(level, ceiling, Direction.DOWN)) return;
        int maxLength = 1 + random.nextInt(2);
        int length = 0;
        while (length < maxLength && accessor.dd$callGetBlock(level, x, y - length, z, chunkBB).isAir()) length++;
        for (int i = 0; i < length; i++) {
            accessor.dd$callPlaceBlock(level, (i == length - 1 ? Blocks.WEEPING_VINES : Blocks.WEEPING_VINES_PLANT).defaultBlockState(), x, y - i, z, chunkBB);
        }
    }

    private static void placeTwistingVines(StructurePieceAccessor accessor, WorldGenLevel level, BoundingBox chunkBB, RandomSource random, int x, int z) {
        BlockPos floor = accessor.dd$callGetWorldPos(x, -1, z);
        if (!chunkBB.isInside(accessor.dd$callGetWorldPos(x, 0, z)) || !level.getBlockState(floor).isFaceSturdy(level, floor, Direction.UP)) return;
        int maxLength = 1 + random.nextInt(2);
        int length = 0;
        while (length < maxLength && accessor.dd$callGetBlock(level, x, length, z, chunkBB).isAir()) length++;
        for (int i = 0; i < length; i++) {
            accessor.dd$callPlaceBlock(level, (i == length - 1 ? Blocks.TWISTING_VINES : Blocks.TWISTING_VINES_PLANT).defaultBlockState(), x, i, z, chunkBB);
        }
    }

    private static void placeRoots(StructurePieceAccessor accessor, WorldGenLevel level, BoundingBox chunkBB, int x, int z, Block plant, Block nylium) {
        if (!accessor.dd$callGetBlock(level, x, 0, z, chunkBB).isAir()) return;
        BlockState floor = accessor.dd$callGetBlock(level, x, -1, z, chunkBB);
        if (floor.is(BlockTags.BASE_STONE_NETHER)) accessor.dd$callPlaceBlock(level, nylium.defaultBlockState(), x, -1, z, chunkBB);
        else if (!floor.is(nylium)) return;
        accessor.dd$callPlaceBlock(level, plant.defaultBlockState(), x, 0, z, chunkBB);
    }

    private static void placeAncientDebris(StructurePieceAccessor accessor, WorldGenLevel level, BoundingBox chunkBB, RandomSource random, int length) {
        int start = random.nextInt(9);
        for (int i = 0; i < 9; i++) {
            int x = (start + i) % 3;
            int y = (start + i) / 3 % 3;
            BlockPos pos = accessor.dd$callGetWorldPos(x, y, length);
            if (!chunkBB.isInside(pos) || !level.getBlockState(pos).is(BlockTags.BASE_STONE_NETHER)) continue;
            if (!accessor.dd$callGetBlock(level, x, y, length - 1, chunkBB).isAir()) continue;
            accessor.dd$callPlaceBlock(level, Blocks.ANCIENT_DEBRIS.defaultBlockState(), x, y, length, chunkBB);
            return;
        }
    }

    private static void spawnPiglinMiner(StructurePieceAccessor accessor, WorldGenLevel level, BoundingBox chunkBB, int length) {
        BlockPos pos = accessor.dd$callGetWorldPos(1, 0, length / 2);
        if (!chunkBB.isInside(pos) || !level.getBlockState(pos).getCollisionShape(level, pos).isEmpty() || !level.getBlockState(pos.above()).isAir()) return;
        //~ if <26.2 'EntityTypes.PIGLIN' -> 'EntityType.PIGLIN'
        Piglin piglin = EntityTypes.PIGLIN.create(level.getLevel(), EntitySpawnReason.CHUNK_GENERATION);
        if (piglin == null) return;
        piglin.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        piglin.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
        piglin.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_PICKAXE));
        piglin.setPersistenceRequired();
        level.addFreshEntity(piglin);
    }

    private static void spawnEnderman(StructurePieceAccessor accessor, WorldGenLevel level, BoundingBox chunkBB, int length) {
        BlockPos pos = accessor.dd$callGetWorldPos(1, 0, length / 2);
        if (!chunkBB.isInside(pos) || !level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()) return;
        if (!level.getBlockState(pos.above()).isAir() || !level.getBlockState(pos.above(2)).isAir()) return;
        //~ if <26.2 'EntityTypes.ENDERMAN' -> 'EntityType.ENDERMAN'
        Mob enderman = EntityTypes.ENDERMAN.create(level.getLevel(), EntitySpawnReason.CHUNK_GENERATION);
        if (enderman == null) return;
        enderman.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        enderman.setPersistenceRequired();
        level.addFreshEntity(enderman);
    }
}
