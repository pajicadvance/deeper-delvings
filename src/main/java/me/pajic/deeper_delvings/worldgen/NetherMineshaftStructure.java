package me.pajic.deeper_delvings.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.pajic.deeper_delvings.util.StructureGenUtil;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class NetherMineshaftStructure extends MineshaftStructure {

    public static final MapCodec<NetherMineshaftStructure> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            settingsCodec(instance),
            Variant.CODEC.fieldOf("variant").forGetter(structure -> structure.variant)
    ).apply(instance, NetherMineshaftStructure::new));

    private final Variant variant;

    public NetherMineshaftStructure(StructureSettings settings, Variant variant) {
        super(settings, variant.mineshaftType());
        this.variant = variant;
    }

    @Override @NotNull
    public Optional<GenerationStub> findGenerationPoint(@NotNull GenerationContext context) {
        return StructureGenUtil.keepPiecesBelow(super.findGenerationPoint(context), context.chunkGenerator().getSeaLevel());
    }

    @Override @NotNull
    public StructureType<?> type() {
        return ModStructures.NETHER_MINESHAFT;
    }

    public enum Variant implements StringRepresentable {
        CRIMSON("crimson"),
        WARPED("warped");

        public static final Codec<Variant> CODEC = StringRepresentable.fromEnum(Variant::values);

        private final String name;

        Variant(String name) {
            this.name = name;
        }

        public MineshaftStructure.Type mineshaftType() {
            return this == CRIMSON ? StructureGenUtil.CRIMSON_MINESHAFT_TYPE : StructureGenUtil.WARPED_MINESHAFT_TYPE;
        }

        @Override @NotNull
        public String getSerializedName() {
            return name;
        }
    }
}
