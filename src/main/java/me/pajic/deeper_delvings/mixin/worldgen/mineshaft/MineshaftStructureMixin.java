package me.pajic.deeper_delvings.mixin.worldgen.mineshaft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.deeper_delvings.DeeperDelvings;
import me.pajic.deeper_delvings.util.StructureGenUtil;
import net.minecraft.util.RandomSource;
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
        int result = original.call(builder, seaLevel, minY, random, offset);
        boolean startsBelowZero = room.getBoundingBox().minY() < 0;
        boolean discard = type == StructureGenUtil.DEEPSLATE_MINESHAFT_TYPE ? !startsBelowZero :
                type == MineshaftStructure.Type.NORMAL && startsBelowZero && DeeperDelvings.CONFIG.deepslateMineshaftVariant.get();
        if (discard) builder.clear();
        return result;
    }
}
