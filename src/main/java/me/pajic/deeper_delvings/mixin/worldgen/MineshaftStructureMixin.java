package me.pajic.deeper_delvings.mixin.worldgen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.deeper_delvings.DeeperDelvings;
import me.pajic.deeper_delvings.util.StructureGenUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MineshaftStructure.class)
public class MineshaftStructureMixin {

    @Shadow @Final private MineshaftStructure.Type type;

    @WrapOperation(
            method = "generatePiecesAndAdjust",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePiecesBuilder;moveBelowSeaLevel(IILnet/minecraft/util/RandomSource;I)I"
            )
    )
    private int adjustMineshaftHeight(
            StructurePiecesBuilder builder,
            int seaLevel,
            int minY,
            RandomSource random,
            int offset,
            Operation<Integer> original,
            @Local(name = "mineShaftRoom") MineshaftPieces.MineShaftRoom room
    ) {
        if (type == StructureGenUtil.DEEPSLATE_MINESHAFT_TYPE) {
            return original.call(builder, offset, minY, random, offset);
        }
        if (type == MineshaftStructure.Type.NORMAL && DeeperDelvings.CONFIG.worldgen.deepslateMineshaftVariant.get()) {
            BoundingBox structureBox = builder.getBoundingBox();
            int roomDepth = structureBox.maxY() - room.getBoundingBox().minY();
            minY = Math.max(minY, roomDepth - structureBox.getYSpan() - 1);
        }
        return original.call(builder, seaLevel, minY, random, offset);
    }
}
