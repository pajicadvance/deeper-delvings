package me.pajic.deeper_delvings.mixin.worldgen.ore;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.deeper_delvings.worldgen.ore.OreSizeScaling;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.OreFeature;
//? if >=26.3 {
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
//?} else {
/*import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
*///?}
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(OreFeature.class)
public class OreFeatureMixin {

    @Unique private static final ThreadLocal<Integer> DD$SCALED_SIZE = new ThreadLocal<>();

    @WrapMethod(method = "place")
    //~ if <26.3 'WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin' -> 'FeaturePlaceContext<OreConfiguration> context'
    private boolean scaleOreSize(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin, Operation<Boolean> original) {
        //~ if <26.3 'OreFeature self = (OreFeature) (Object) this' -> 'OreConfiguration self = context.config()'
        OreFeature self = (OreFeature) (Object) this;
        //~ if <26.3 'level.getLevel()' -> 'context.level().getLevel()'
        boolean overworld = level.getLevel().dimension() == Level.OVERWORLD;
        //~ if <26.3 'self.targetStates().stream().map(target -> target.state())' -> 'self.targetStates.stream().map(target -> target.state)'
        List<BlockState> targets = self.targetStates().stream().map(target -> target.state()).toList();
        //~ if <26.3 'origin.getY()' -> 'context.origin().getY()'
        Float multiplier = overworld ? OreSizeScaling.getMultiplier(targets, origin.getY()) : null;
        //~ if <26.3 'size() * multiplier' -> 'size * multiplier'
        if (multiplier != null) DD$SCALED_SIZE.set(Math.round(self.size() * multiplier));
        try {
            //~ if <26.3 'original.call(level, chunkGenerator, random, origin)' -> 'original.call(context)'
            return original.call(level, chunkGenerator, random, origin);
        } finally {
            DD$SCALED_SIZE.remove();
        }
    }

    @ModifyExpressionValue(
            method = {"place", "doPlace"},
            at = @At(
                    value = "FIELD",
                    //~ if <26.3 'feature/OreFeature;' -> 'feature/configurations/OreConfiguration;'
                    target = "Lnet/minecraft/world/level/levelgen/feature/OreFeature;size:I",
                    opcode = Opcodes.GETFIELD
            )
    )
    private int useScaledSize(int original) {
        Integer scaledSize = DD$SCALED_SIZE.get();
        return scaledSize != null ? scaledSize : original;
    }
}
