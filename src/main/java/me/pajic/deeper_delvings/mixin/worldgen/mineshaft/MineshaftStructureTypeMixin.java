package me.pajic.deeper_delvings.mixin.worldgen.mineshaft;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MineshaftStructure.Type.class)
public enum MineshaftStructureTypeMixin {

    DEEPER_DELVINGS_DEEPSLATE("deeper_delvings_deepslate", Blocks.SPRUCE_LOG, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_FENCE),
    DEEPER_DELVINGS_CRIMSON("deeper_delvings_crimson", Blocks.CRIMSON_STEM, Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_FENCE),
    DEEPER_DELVINGS_WARPED("deeper_delvings_warped", Blocks.WARPED_STEM, Blocks.WARPED_PLANKS, Blocks.WARPED_FENCE);

    @Shadow MineshaftStructureTypeMixin(String name, Block wood, Block plank, Block fence) {}
}
