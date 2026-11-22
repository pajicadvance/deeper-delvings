package me.pajic.deeper_delvings.worldgen.mineshaft;

import me.pajic.deeper_delvings.DeeperDelvings;
import me.pajic.deeper_delvings.mixin.worldgen.StructurePieceAccessor;
import me.pajic.deeper_delvings.platform.MultiLoaderUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
//~ if <26.2 'entity.EntityTypes;' -> 'entity.EntityType;'
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.storage.loot.LootTable;

public class DeepslateMineshaftDecorator {

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, DeeperDelvings.id("chests/deepslate_mineshaft"));
    private static final boolean WALL_LANTERNS_LOADED = MultiLoaderUtil.INSTANCE.isModLoaded("walllanterns");
    private static final Identifier WALL_LANTERN = Identifier.fromNamespaceAndPath("walllanterns", "wall_lantern");
    private static final float ACTIVATOR_RAIL_CHANCE = 0.03F;
    private static final float TNT_MINECART_CHANCE = 0.015F;
    private static final float CHEST_MINECART_MULTIPLIER = 2F;

    public static BlockState lantern(StructurePiece piece, BlockState torch) {
        if (WALL_LANTERNS_LOADED) {
            Mirror mirror = piece.getMirror() == null ? Mirror.NONE : piece.getMirror();
            Rotation rotation = piece.getRotation() == null ? Rotation.NONE : piece.getRotation();
            Direction facing = torch.mirror(mirror).rotate(rotation).getValue(WallTorchBlock.FACING);
            return BuiltInRegistries.BLOCK.getValue(WALL_LANTERN).defaultBlockState()
                    .setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
        }
        return Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true);
    }

    public static float reduceRailGapChance(float chance) {
        return chance + (1 - chance) * 0.8F;
    }

    public static int chestMinecartChance(int original) {
        return Math.max(1, Math.round(original / CHEST_MINECART_MULTIPLIER));
    }

    public static boolean shouldPlaceActivatorRail(RandomSource random) {
        return random.nextFloat() < ACTIVATOR_RAIL_CHANCE;
    }

    public static BlockState activatorRail(BlockState rail) {
        return Blocks.ACTIVATOR_RAIL.defaultBlockState().setValue(PoweredRailBlock.SHAPE, rail.getValue(RailBlock.SHAPE));
    }

    public static void powerActivatorRail(StructurePiece piece, WorldGenLevel level, BoundingBox chunkBB, RandomSource random, int x, int y, int z, BlockState activatorRail) {
        StructurePieceAccessor accessor = (StructurePieceAccessor) piece;
        if (accessor.dd$callGetBlock(level, x, y, z, chunkBB).is(Blocks.ACTIVATOR_RAIL) && placeRedstoneTorch(accessor, level, chunkBB, random, x, y, z)) {
            accessor.dd$callPlaceBlock(level, activatorRail.setValue(PoweredRailBlock.POWERED, true), x, y, z, chunkBB);
        }
    }

    public static void maybeSpawnTntMinecart(StructurePiece piece, WorldGenLevel level, BoundingBox chunkBB, RandomSource random, int x, int y, int z) {
        StructurePieceAccessor accessor = (StructurePieceAccessor) piece;
        if (!accessor.dd$callGetBlock(level, x, y, z, chunkBB).is(Blocks.RAIL) || random.nextFloat() >= TNT_MINECART_CHANCE) return;
        //~ if <26.2 'EntityTypes.TNT_MINECART' -> 'EntityType.TNT_MINECART'
        MinecartTNT minecart = EntityTypes.TNT_MINECART.create(level.getLevel(), EntitySpawnReason.CHUNK_GENERATION);
        if (minecart == null) return;
        BlockPos pos = accessor.dd$callGetWorldPos(x, y, z);
        minecart.setInitialPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        level.addFreshEntity(minecart);
    }

    private static boolean placeRedstoneTorch(StructurePieceAccessor accessor, WorldGenLevel level, BoundingBox chunkBB, RandomSource random, int x, int y, int z) {
        int first = random.nextBoolean() ? -1 : 1;
        for (int side : new int[]{first, -first}) {
            BlockPos pos = accessor.dd$callGetWorldPos(x + side, y, z);
            BlockState torch = Blocks.REDSTONE_TORCH.defaultBlockState();
            if (chunkBB.isInside(pos) && level.getBlockState(pos).isAir() && torch.canSurvive(level, pos)) {
                accessor.dd$callPlaceBlock(level, torch, x + side, y, z, chunkBB);
                return true;
            }
        }
        return false;
    }
}
