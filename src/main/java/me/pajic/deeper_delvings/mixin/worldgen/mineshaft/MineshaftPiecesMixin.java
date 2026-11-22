package me.pajic.deeper_delvings.mixin.worldgen.mineshaft;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.deeper_delvings.util.StructureGenUtil;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MineshaftPieces.class)
public class MineshaftPiecesMixin {

    @ModifyExpressionValue(
            method = "generateAndAddPiece",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=8"
            )
    )
    private static int increaseNetherMineshaftDepth(int original, @Local(argsOnly = true, name = "startPiece") StructurePiece startPiece) {
        return StructureGenUtil.isNetherMineshaftPiece(startPiece) ? StructureGenUtil.NETHER_MINESHAFT_MAX_DEPTH : original;
    }

    @ModifyExpressionValue(
            method = "generateAndAddPiece",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=80"
            )
    )
    private static int increaseNetherMineshaftRadius(int original, @Local(argsOnly = true, name = "startPiece") StructurePiece startPiece) {
        return StructureGenUtil.isNetherMineshaftPiece(startPiece) ? StructureGenUtil.NETHER_MINESHAFT_RADIUS : original;
    }
}
