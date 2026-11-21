package me.pajic.deeper_delvings.mixin.worldgen;

import me.pajic.deeper_delvings.util.StructureGenUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MineshaftPieces.MineShaftRoom.class)
public class MineShaftRoomMixin {

    @Inject(
            method = "postProcess",
            at = @At("TAIL")
    )
    private void connectToLavaPool(
            WorldGenLevel level,
            StructureManager structureManager,
            ChunkGenerator generator,
            RandomSource random,
            BoundingBox chunkBB,
            ChunkPos chunkPos,
            BlockPos referencePos,
            CallbackInfo ci
    ) {
        StructurePiece room = (StructurePiece) (Object) this;
        if (StructureGenUtil.isNetherMineshaftPiece(room)) StructureGenUtil.placeLavaPoolShaft(level, chunkBB, room.getBoundingBox(), ((MineShaftPieceAccessor) room).ac$getType().getWoodState());
    }
}
