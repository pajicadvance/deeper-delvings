package me.pajic.deeper_delvings.util;

import com.mojang.datafixers.util.Either;
import me.pajic.deeper_delvings.mixin.worldgen.mineshaft.MineShaftPieceAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class StructureGenUtil {

    public static final MineshaftStructure.Type DEEPSLATE_MINESHAFT_TYPE = MineshaftStructure.Type.valueOf("DEEPER_DELVINGS_DEEPSLATE");
    public static final MineshaftStructure.Type CRIMSON_MINESHAFT_TYPE = MineshaftStructure.Type.valueOf("DEEPER_DELVINGS_CRIMSON");
    public static final MineshaftStructure.Type WARPED_MINESHAFT_TYPE = MineshaftStructure.Type.valueOf("DEEPER_DELVINGS_WARPED");

    public static final int NETHER_MINESHAFT_MAX_DEPTH = 10;
    public static final int NETHER_MINESHAFT_RADIUS = 96;
    private static final float LAVA_POOL_SHAFT_CHANCE = 0.5F;
    private static final int LAVA_POOL_SHAFT_MAX_LENGTH = 24;
    private static final int LAVA_POOL_RADIUS = 5;
    private static final int LAVA_POOL_MIN_SOURCES = 60;
    private static final int LAVA_POOL_MIN_DEPTH = 3;

    public static boolean isDeepslateMineshaftPiece(StructurePiece piece) {
        return piece instanceof MineShaftPieceAccessor accessor && accessor.dd$getType() == DEEPSLATE_MINESHAFT_TYPE;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static Optional<Structure.GenerationStub> keepPiecesBelow(Optional<Structure.GenerationStub> generationStub, int y) {
        return generationStub.map(stub -> {
            StructurePiecesBuilder kept = new StructurePiecesBuilder();
            stub.getPiecesBuilder().build().pieces().stream()
                    .filter(piece -> piece.getBoundingBox().maxY() < y)
                    .forEach(kept::addPiece);
            return new Structure.GenerationStub(stub.position(), Either.right(kept));
        }).filter(stub -> !stub.getPiecesBuilder().isEmpty());
    }

    public static boolean isNetherMineshaftPiece(StructurePiece piece) {
        return isCrimsonMineshaftPiece(piece) || isWarpedMineshaftPiece(piece);
    }

    public static boolean isCrimsonMineshaftPiece(StructurePiece piece) {
        return piece instanceof MineShaftPieceAccessor accessor && accessor.dd$getType() == CRIMSON_MINESHAFT_TYPE;
    }

    public static boolean isWarpedMineshaftPiece(StructurePiece piece) {
        return piece instanceof MineShaftPieceAccessor accessor && accessor.dd$getType() == WARPED_MINESHAFT_TYPE;
    }

    public static void placeLavaPoolShaft(WorldGenLevel level, BoundingBox chunkBB, BoundingBox roomBB, BlockState pillar) {
        int centerX = (roomBB.minX() + roomBB.maxX()) / 2;
        int centerZ = (roomBB.minZ() + roomBB.maxZ()) / 2;
        int chunkMinX = SectionPos.sectionToBlockCoord(SectionPos.blockToSectionCoord(centerX));
        int chunkMinZ = SectionPos.sectionToBlockCoord(SectionPos.blockToSectionCoord(centerZ));
        BlockPos ladderBase = new BlockPos(
                Math.clamp(centerX, chunkMinX + 1, chunkMinX + 14),
                roomBB.minY() + 1,
                Math.clamp(centerZ, chunkMinZ + 1, chunkMinZ + 14)
        );
        if (!chunkBB.isInside(ladderBase)) return;
        RandomSource random = RandomSource.create(level.getSeed() ^ BlockPos.asLong(roomBB.minX(), roomBB.minY(), roomBB.minZ()));
        if (random.nextFloat() >= LAVA_POOL_SHAFT_CHANCE) return;
        Direction facing = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        BlockPos pillarBase = ladderBase.relative(facing.getOpposite());

        BlockPos.MutableBlockPos pos = ladderBase.mutable();
        if (!level.getBlockState(pos).isAir()) return;
        while (level.getBlockState(pos).isAir()) {
            if (pos.getY() > roomBB.maxY()) return;
            pos.move(Direction.UP);
        }
        int roomCeiling = pos.getY();
        int lavaBottom = -1;
        for (int y = roomCeiling; y <= roomCeiling + LAVA_POOL_SHAFT_MAX_LENGTH; y++) {
            pos.setY(y);
            BlockState state = level.getBlockState(pos);
            if (isLavaSource(state.getFluidState())) {
                lavaBottom = y;
                break;
            }
            if (state.isAir() || !state.getFluidState().isEmpty()) return;
        }
        if (lavaBottom < 0 || !isLargeLavaPool(level, pos.setY(lavaBottom))) return;

        for (int y = ladderBase.getY(); y < lavaBottom; y++) {
            BlockPos ladderPos = ladderBase.atY(y);
            BlockPos supportPos = pillarBase.atY(y);
            if (y < roomCeiling) {
                level.setBlock(supportPos, pillar, 2);
            } else {
                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    BlockPos wallPos = ladderPos.relative(direction);
                    if (!level.getBlockState(wallPos).isFaceSturdy(level, wallPos, direction.getOpposite())) {
                        level.setBlock(wallPos, Blocks.NETHERRACK.defaultBlockState(), 2);
                    }
                }
            }
            level.setBlock(ladderPos, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, facing), 2);
        }
    }

    private static boolean isLargeLavaPool(WorldGenLevel level, BlockPos bottom) {
        BlockPos.MutableBlockPos surface = bottom.mutable();
        while (isLavaSource(level.getFluidState(surface.above()))) surface.move(Direction.UP);
        if (surface.getY() - bottom.getY() + 1 < LAVA_POOL_MIN_DEPTH) return false;
        int sources = 0;
        BlockPos layer = surface.immutable();
        for (BlockPos pos : BlockPos.betweenClosed(layer.offset(-LAVA_POOL_RADIUS, 0, -LAVA_POOL_RADIUS), layer.offset(LAVA_POOL_RADIUS, 0, LAVA_POOL_RADIUS))) {
            if (isLavaSource(level.getFluidState(pos))) sources++;
        }
        return sources >= LAVA_POOL_MIN_SOURCES;
    }

    private static boolean isLavaSource(FluidState fluidState) {
        return fluidState.is(Fluids.LAVA) && fluidState.isSource();
    }

    public static void placeChestsAtRandomLocations(
            BoundingBox chunkBoundingBox,
            BoundingBox structureBoundingBox,
            WorldGenLevel worldGenLevel,
            RandomSource randomSource,
            BiPredicate<BlockState, BlockPos> predicateOn,
            Predicate<BlockState> predicateAt,
            float chance,
            ResourceKey<LootTable> lootTable
    ) {
        if (!chunkBoundingBox.intersects(structureBoundingBox)) return;
        int minX = Math.max(chunkBoundingBox.minX(), structureBoundingBox.minX());
        int maxX = Math.min(chunkBoundingBox.maxX(), structureBoundingBox.maxX());
        int minY = Math.max(chunkBoundingBox.minY(), structureBoundingBox.minY());
        int maxY = Math.min(chunkBoundingBox.maxY(), structureBoundingBox.maxY());
        int minZ = Math.max(chunkBoundingBox.minZ(), structureBoundingBox.minZ());
        int maxZ = Math.min(chunkBoundingBox.maxZ(), structureBoundingBox.maxZ());
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    pos.set(x, y, z);
                    if (predicateOn.test(worldGenLevel.getBlockState(pos), pos)) {
                        BlockPos pos1 = pos.above();
                        if (predicateAt.test(worldGenLevel.getBlockState(pos1)) && isEnclosed(pos1, worldGenLevel, structureBoundingBox, predicateAt)) {
                            Direction dir = checkSides(pos1, worldGenLevel, predicateAt);
                            if (dir != null && randomSource.nextFloat() < chance) {
                                createChest(worldGenLevel, chunkBoundingBox, randomSource, pos1, dir, lootTable);
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean isEnclosed(BlockPos pos, WorldGenLevel level, BoundingBox structureBoundingBox, Predicate<BlockState> predicateAt) {
        BlockPos.MutableBlockPos pos1 = pos.mutable().move(Direction.UP);
        while (structureBoundingBox.isInside(pos1)) {
            BlockState state = level.getBlockState(pos1);
            if (!predicateAt.test(state) && state.isFaceSturdy(level, pos1, Direction.DOWN)) return true;
            pos1.move(Direction.UP);
        }
        return false;
    }

    @Nullable
    private static Direction checkSides(BlockPos pos, WorldGenLevel level, Predicate<BlockState> predicate) {
        Map<Direction, Boolean> states = new EnumMap<>(Direction.class);
        for (Direction dir : Direction.Plane.HORIZONTAL) states.put(dir, predicate.test(level.getBlockState(pos.relative(dir))));
        if (states.containsValue(false)) {
            for (Direction dir : Direction.Plane.HORIZONTAL) if (states.get(dir) && !states.get(dir.getOpposite())) return dir;
            for (Direction dir : Direction.Plane.HORIZONTAL) if (states.get(dir)) return dir;
        }
        return null;
    }

    private static void createChest(
            ServerLevelAccessor serverLevelAccessor,
            BoundingBox boundingBox,
            RandomSource randomSource,
            BlockPos blockPos,
            Direction direction,
            ResourceKey<LootTable> resourceKey
    ) {
        if (boundingBox.isInside(blockPos) && !serverLevelAccessor.getBlockState(blockPos).is(Blocks.CHEST)) {
            serverLevelAccessor.setBlock(blockPos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, direction), 2);
            BlockEntity blockEntity = serverLevelAccessor.getBlockEntity(blockPos);
            if (blockEntity instanceof ChestBlockEntity) {
                ((ChestBlockEntity) blockEntity).setLootTable(resourceKey, randomSource.nextLong());
            }
        }
    }
}
