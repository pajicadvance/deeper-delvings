package me.pajic.deeper_delvings.mixin.worldgen.bonuschest;

import me.pajic.deeper_delvings.DeeperDelvings;
import me.pajic.deeper_delvings.util.StructureGenUtil;
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
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WoodlandMansionStructure.class)
public class WoodlandMansionStructureMixin {

    @Inject(
            method = "afterPlace",
            at = @At("TAIL")
    )
    private void addChestsToRandomLocations(
            WorldGenLevel level,
            StructureManager structureManager,
            ChunkGenerator generator,
            RandomSource random,
            BoundingBox chunkBB,
            ChunkPos chunkPos,
            PiecesContainer pieces,
            CallbackInfo ci
    ) {
        if (DeeperDelvings.CONFIG.bonusChests.mansionChests.get()) {
            StructureGenUtil.placeChestsAtRandomLocations(
                    chunkBB,
                    pieces.calculateBoundingBox(),
                    level,
                    random,
                    (state, _) -> state.is(Blocks.BIRCH_PLANKS) || state.is(Blocks.OAK_PLANKS) || state.is(Blocks.BOOKSHELF),
                    state -> state.is(BlockTags.AIR),
                    DeeperDelvings.CONFIG.bonusChests.mansionChance.get(),
                    ResourceKey.create(Registries.LOOT_TABLE, DeeperDelvings.id("chests/woodland_mansion"))
            );
        }
    }
}
