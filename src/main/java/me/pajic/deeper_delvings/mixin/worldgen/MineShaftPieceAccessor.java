package me.pajic.deeper_delvings.mixin.worldgen;

import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces$MineShaftPiece")
public interface MineShaftPieceAccessor {

    @Accessor("type")
    MineshaftStructure.Type ac$getType();
}
