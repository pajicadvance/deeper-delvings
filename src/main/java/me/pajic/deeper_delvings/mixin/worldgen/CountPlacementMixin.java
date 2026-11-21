package me.pajic.deeper_delvings.mixin.worldgen;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.deeper_delvings.worldgen.SpawnRateMultipliers;
import me.pajic.deeper_delvings.worldgen.SpawnRateScalable;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Supplier;

@Mixin(CountPlacement.class)
public class CountPlacementMixin implements SpawnRateScalable {

    @Unique private @Nullable Supplier<Float> spawnRateMultiplier;

    @Override
    public void deeper_delvings$setSpawnRateMultiplier(Supplier<Float> multiplier) {
        spawnRateMultiplier = multiplier;
    }

    @ModifyReturnValue(
            method = "count(Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;)I",
            at = @At("RETURN")
    )
    private int scaleCount(int original, @Local(argsOnly = true, name = "random") RandomSource random) {
        return spawnRateMultiplier == null ? original : SpawnRateMultipliers.scaleCount(original, spawnRateMultiplier.get(), random);
    }
}
