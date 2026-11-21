package me.pajic.deeper_delvings.worldgen;

import com.mojang.serialization.MapCodec;
import me.pajic.deeper_delvings.util.StructureGenUtil;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class DeepslateMineshaftStructure extends MineshaftStructure {

    public static final MapCodec<DeepslateMineshaftStructure> CODEC = simpleCodec(DeepslateMineshaftStructure::new);

    public DeepslateMineshaftStructure(StructureSettings settings) {
        super(settings, StructureGenUtil.DEEPSLATE_MINESHAFT_TYPE);
    }

    @Override @NotNull
    public Optional<GenerationStub> findGenerationPoint(@NotNull GenerationContext context) {
        return StructureGenUtil.keepPiecesBelow(super.findGenerationPoint(context), 0);
    }

    @Override @NotNull
    public StructureType<?> type() {
        return ModStructures.DEEPSLATE_MINESHAFT;
    }
}
