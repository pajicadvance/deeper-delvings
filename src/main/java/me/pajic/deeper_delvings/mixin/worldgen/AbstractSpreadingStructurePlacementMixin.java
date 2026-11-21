package me.pajic.deeper_delvings.mixin.worldgen;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.pajic.deeper_delvings.worldgen.SpawnRateScalable;
//? if >=26.3 {
import net.minecraft.world.level.levelgen.structure.placement.AbstractSpreadingStructurePlacement;
//?} else {
/*import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
*///?}
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Supplier;

//? if >=26.3 {
@Mixin(AbstractSpreadingStructurePlacement.class)
//?} else {
/*@Mixin(StructurePlacement.class)
*///?}
public class AbstractSpreadingStructurePlacementMixin implements SpawnRateScalable {

    @Unique private @Nullable Supplier<Float> spawnRateMultiplier;

    @Override
    public void deeper_delvings$setSpawnRateMultiplier(Supplier<Float> multiplier) {
        spawnRateMultiplier = multiplier;
    }

    @ModifyExpressionValue(
            method = "applyAdditionalChunkRestrictions",
            at = @At(
                    value = "FIELD",
                    //? if >=26.3 {
                    target = "Lnet/minecraft/world/level/levelgen/structure/placement/AbstractSpreadingStructurePlacement;frequency:F",
                    //?} else {
                    /*target = "Lnet/minecraft/world/level/levelgen/structure/placement/StructurePlacement;frequency:F",
                    *///?}
                    opcode = Opcodes.GETFIELD
            )
    )
    private float scaleFrequency(float original) {
        return spawnRateMultiplier == null ? original : original * spawnRateMultiplier.get();
    }
}
