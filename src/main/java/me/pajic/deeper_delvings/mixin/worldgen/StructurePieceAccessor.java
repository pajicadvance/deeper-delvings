package me.pajic.deeper_delvings.mixin.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StructurePiece.class)
public interface StructurePieceAccessor {

    @Invoker("getWorldPos")
    BlockPos.MutableBlockPos ac$getWorldPos(int x, int y, int z);

    @Invoker("placeBlock")
    void ac$placeBlock(WorldGenLevel level, BlockState state, int x, int y, int z, BoundingBox chunkBB);

    @Invoker("getBlock")
    BlockState ac$getBlock(BlockGetter level, int x, int y, int z, BoundingBox chunkBB);
}
