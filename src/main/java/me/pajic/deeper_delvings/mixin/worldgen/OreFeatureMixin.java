package me.pajic.deeper_delvings.mixin.worldgen;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.deeper_delvings.DeeperDelvings;
import me.pajic.deeper_delvings.config.ModConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.OreFeature;
//? if >=26.3 {
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.AbstractOreFeature;
//?} else {
/*import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
*///?}
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OreFeature.class)
public class OreFeatureMixin {

    @Unique private static final TagKey<Block> ORES = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ores"));
    @Unique private static final ThreadLocal<Integer> DD$SCALED_SIZE = new ThreadLocal<>();

    //? if >=26.3 {
    @WrapMethod(method = "place")
    private boolean scaleOreSize(
            WorldGenLevel level,
            ChunkGenerator chunkGenerator,
            RandomSource random,
            BlockPos origin,
            Operation<Boolean> original
    ) {
        AbstractOreFeature self = (AbstractOreFeature) (Object) this;
        if (level.getLevel().dimension() != Level.OVERWORLD || self.targetStates().stream().noneMatch(target -> target.state().is(ORES))) {
            return original.call(level, chunkGenerator, random, origin);
        }
        DD$SCALED_SIZE.set(Math.round(self.size() * dd$getSizeMult(origin.getY())));
        boolean result = original.call(level, chunkGenerator, random, origin);
        DD$SCALED_SIZE.remove();
        return result;
    }

    @ModifyExpressionValue(
            method = {"place", "doPlace"},
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/levelgen/feature/OreFeature;size:I",
                    opcode = Opcodes.GETFIELD
            )
    )
    private int useScaledSize(int original) {
        Integer scaledSize = DD$SCALED_SIZE.get();
        return scaledSize != null ? scaledSize : original;
    }
    //?} else {
    /*@WrapMethod(method = "place")
    private boolean scaleOreSize(FeaturePlaceContext<OreConfiguration> context, Operation<Boolean> original) {
        OreConfiguration config = context.config();
        if (context.level().getLevel().dimension() != Level.OVERWORLD || config.targetStates.stream().noneMatch(target -> target.state.is(ORES))) {
            return original.call(context);
        }
        DD$SCALED_SIZE.set(Math.round(config.size * dd$getSizeMult(context.origin().getY())));
        boolean result = original.call(context);
        DD$SCALED_SIZE.remove();
        return result;
    }

    @ModifyExpressionValue(
            method = {"place", "doPlace"},
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;size:I",
                    opcode = Opcodes.GETFIELD
            )
    )
    private int useScaledSize(int original) {
        Integer scaledSize = DD$SCALED_SIZE.get();
        return scaledSize != null ? scaledSize : original;
    }
    *///?}

    @Unique
    private static float dd$getSizeMult(int y) {
        ModConfig.Worldgen config = DeeperDelvings.CONFIG.worldgen;
        int maxY = config.oreSizeMultMaxY.get();
        int midY = config.oreSizeMultMidY.get();
        int minY = config.oreSizeMultMinY.get();
        float maxMult = config.oreSizeMaxMult.get();
        float minMult = config.oreSizeMinMult.get();
        if (y >= maxY) return minMult;
        if (y <= minY) return maxMult;
        if (y >= midY) return Mth.lerp((float) (y - midY) / (maxY - midY), 1.0F, minMult);
        return Mth.lerp((float) (midY - y) / (midY - minY), 1.0F, maxMult);
    }
}
