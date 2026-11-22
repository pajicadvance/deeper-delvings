package me.pajic.deeper_delvings.mixin.worldgen.bonuschest;

import me.pajic.deeper_delvings.DeeperDelvings;
import me.pajic.deeper_delvings.util.StructureGenUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OceanMonumentPieces.MonumentBuilding.class)
public class MonumentBuildingMixin {

    @Inject(
            method = "postProcess",
            at = @At("TAIL")
    )
    private void addChestsToRandomLocations(
            WorldGenLevel level,
            StructureManager structureManager,
            ChunkGenerator generator,
            RandomSource random,
            BoundingBox chunkBB,
            ChunkPos chunkPos,
            BlockPos referencePos,
            CallbackInfo ci
    ) {
        if (DeeperDelvings.CONFIG.bonusChests.monumentChests.get()) {
            StructureGenUtil.placeChestsAtRandomLocations(
                    chunkBB,
                    ((StructurePiece) (Object) this).getBoundingBox(),
                    level,
                    random,
                    (state, _) -> state.is(Blocks.PRISMARINE) || state.is(Blocks.PRISMARINE_BRICKS),
                    state -> state.is(BlockTags.AIR) || state.is(Blocks.WATER),
                    DeeperDelvings.CONFIG.bonusChests.monumentChance.get(),
                    ResourceKey.create(Registries.LOOT_TABLE, DeeperDelvings.id("chests/ocean_monument"))
            );
        }
    }
}
