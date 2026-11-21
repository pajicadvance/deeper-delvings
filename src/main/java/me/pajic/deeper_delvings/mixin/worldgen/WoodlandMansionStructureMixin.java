package me.pajic.deeper_delvings.mixin.worldgen;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
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

@Mixin(WoodlandMansionStructure.class)
public class WoodlandMansionStructureMixin {

    @WrapMethod(method = "afterPlace")
    private void addChestsToRandomLocations(
            WorldGenLevel level,
            StructureManager structureManager,
            ChunkGenerator generator,
            RandomSource random,
            BoundingBox chunkBB,
            ChunkPos chunkPos,
            PiecesContainer pieces,
            Operation<Void> original
    ) {
        original.call(level, structureManager, generator, random, chunkBB, chunkPos, pieces);
        if (DeeperDelvings.CONFIG.worldgen.addRandomChestsToMansions.get()) {
            StructureGenUtil.placeChestsAtRandomLocations(
                    chunkBB,
                    pieces.calculateBoundingBox(),
                    level,
                    random,
                    (state, _) -> state.is(Blocks.BIRCH_PLANKS) || state.is(Blocks.OAK_PLANKS) || state.is(Blocks.BOOKSHELF),
                    state -> state.is(BlockTags.AIR),
                    DeeperDelvings.CONFIG.worldgen.mansionRandomChestChance.get(),
                    ResourceKey.create(Registries.LOOT_TABLE, DeeperDelvings.id("chests/woodland_mansion"))
            );
        }
    }
}
