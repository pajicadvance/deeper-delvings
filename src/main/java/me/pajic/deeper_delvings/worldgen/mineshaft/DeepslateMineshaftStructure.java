package me.pajic.deeper_delvings.worldgen.mineshaft;

import com.mojang.serialization.MapCodec;
import me.pajic.deeper_delvings.util.StructureGenUtil;
import me.pajic.deeper_delvings.worldgen.ModStructures;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
import org.jetbrains.annotations.NotNull;

public class DeepslateMineshaftStructure extends MineshaftStructure {

    public static final MapCodec<DeepslateMineshaftStructure> CODEC = simpleCodec(DeepslateMineshaftStructure::new);

    public DeepslateMineshaftStructure(StructureSettings settings) {
        super(settings, StructureGenUtil.DEEPSLATE_MINESHAFT_TYPE);
    }

    @Override @NotNull
    public StructureType<?> type() {
        return ModStructures.DEEPSLATE_MINESHAFT;
    }
}
