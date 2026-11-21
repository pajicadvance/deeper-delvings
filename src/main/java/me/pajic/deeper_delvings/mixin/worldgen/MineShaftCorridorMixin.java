package me.pajic.deeper_delvings.mixin.worldgen;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.deeper_delvings.entity.ModEntities;
import me.pajic.deeper_delvings.util.StructureGenUtil;
import me.pajic.deeper_delvings.worldgen.DeepslateMineshaftDecorator;
import me.pajic.deeper_delvings.worldgen.NetherMineshaftDecorator;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MineshaftPieces.MineShaftCorridor.class)
public abstract class MineShaftCorridorMixin extends StructurePiece {

    @Shadow @Final private int numSections;

    protected MineShaftCorridorMixin(StructurePieceType type, int genDepth, BoundingBox boundingBox) {
        super(type, genDepth, boundingBox);
    }

    @ModifyArg(
            method = "placeSupport",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftCorridor;maybeGenerateBlock(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/util/RandomSource;FIIILnet/minecraft/world/level/block/state/BlockState;)V"
            ),
            index = 7
    )
    private BlockState replaceLight(BlockState torch) {
        if (StructureGenUtil.isNetherMineshaftPiece(this)) return Blocks.SHROOMLIGHT.defaultBlockState();
        return StructureGenUtil.isDeepslateMineshaftPiece(this) ? DeepslateMineshaftDecorator.lantern(this, torch) : torch;
    }

    @ModifyExpressionValue(
            method = "<init>(ILnet/minecraft/util/RandomSource;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/core/Direction;Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftStructure$Type;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/RandomSource;nextInt(I)I",
                    ordinal = 0
            )
    )
    private int increaseRailChance(int original, @Local(argsOnly = true, name = "type") MineshaftStructure.Type type) {
        return type == StructureGenUtil.DEEPSLATE_MINESHAFT_TYPE && original == 1 ? 0 : original;
    }

    @ModifyExpressionValue(
            method = "postProcess",
            at = {
                    @At(value = "CONSTANT", args = "floatValue=0.7"),
                    @At(value = "CONSTANT", args = "floatValue=0.9")
            }
    )
    private float reduceRailGaps(float original) {
        return StructureGenUtil.isDeepslateMineshaftPiece(this) ? DeepslateMineshaftDecorator.reduceRailGapChance(original) : original;
    }

    @ModifyExpressionValue(
            method = "postProcess",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=100"
            )
    )
    private int increaseChestMinecartChance(int original) {
        return StructureGenUtil.isDeepslateMineshaftPiece(this) ? DeepslateMineshaftDecorator.chestMinecartChance(original) : original;
    }

    @WrapOperation(
            method = "postProcess",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftCorridor;maybeGenerateBlock(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/util/RandomSource;FIIILnet/minecraft/world/level/block/state/BlockState;)V"
            )
    )
    private void placeDeepslateRails(
            MineshaftPieces.MineShaftCorridor instance,
            WorldGenLevel level,
            BoundingBox chunkBB,
            RandomSource random,
            float chance,
            int x,
            int y,
            int z,
            BlockState rail,
            Operation<Void> original
    ) {
        if (!StructureGenUtil.isDeepslateMineshaftPiece(this)) {
            original.call(instance, level, chunkBB, random, chance, x, y, z, rail);
        } else if (DeepslateMineshaftDecorator.shouldPlaceActivatorRail(random)) {
            BlockState activatorRail = DeepslateMineshaftDecorator.activatorRail(rail);
            original.call(instance, level, chunkBB, random, chance, x, y, z, activatorRail);
            DeepslateMineshaftDecorator.powerActivatorRail(this, level, chunkBB, random, x, y, z, activatorRail);
        } else {
            original.call(instance, level, chunkBB, random, chance, x, y, z, rail);
            DeepslateMineshaftDecorator.maybeSpawnTntMinecart(this, level, chunkBB, random, x, y, z);
        }
    }

    @ModifyArg(
            method = "postProcess",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/SpawnerBlockEntity;setEntityId(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/util/RandomSource;)V"
            ),
            index = 0
    )
    private EntityType<?> replaceSpiderSpawner(EntityType<?> original) {
        return StructureGenUtil.isNetherMineshaftPiece(this) ? ModEntities.FIERY_SPIDER : original;
    }

    @ModifyArg(
            method = "postProcess",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftCorridor;createChest(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/resources/ResourceKey;)Z"
            ),
            index = 6
    )
    private ResourceKey<LootTable> replaceChestMinecartLoot(ResourceKey<LootTable> original) {
        return NetherMineshaftDecorator.lootTable(this, original);
    }

    @ModifyExpressionValue(
            method = "postProcess",
            at = @At(
                    value = "CONSTANT",
                    args = "floatValue=0.6"
            )
    )
    private float thinSpiderCorridorCobwebs(float original) {
        return StructureGenUtil.isNetherMineshaftPiece(this) ? NetherMineshaftDecorator.spiderCorridorCobwebChance() : original;
    }

    @WrapOperation(
            method = "postProcess",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftCorridor;maybePlaceCobWeb(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/util/RandomSource;FIII)V"
            )
    )
    private void replaceFoliage(
            MineshaftPieces.MineShaftCorridor instance,
            WorldGenLevel level,
            BoundingBox chunkBB,
            RandomSource random,
            float probability,
            int x,
            int y,
            int z,
            Operation<Void> original
    ) {
        if (StructureGenUtil.isNetherMineshaftPiece(this)) NetherMineshaftDecorator.placeFoliage(this, level, chunkBB, random, probability, x, y, z);
        else original.call(instance, level, chunkBB, random, probability, x, y, z);
    }

    @Inject(
            method = "postProcess",
            at = @At("TAIL")
    )
    private void decorateNetherCorridor(
            WorldGenLevel level,
            StructureManager structureManager,
            ChunkGenerator generator,
            RandomSource random,
            BoundingBox chunkBB,
            ChunkPos chunkPos,
            BlockPos referencePos,
            CallbackInfo ci
    ) {
        if (StructureGenUtil.isNetherMineshaftPiece(this)) NetherMineshaftDecorator.decorateCorridor(this, level, chunkBB, numSections);
    }
}
