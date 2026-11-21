package me.pajic.deeper_delvings.worldgen;

import me.pajic.deeper_delvings.DeeperDelvings;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ModStructures {

    private static final Map<ResourceKey<StructureType<?>>, StructureType<?>> STRUCTURE_TYPES = new LinkedHashMap<>();

    public static final StructureType<DeepslateMineshaftStructure> DEEPSLATE_MINESHAFT = create(
            "deepslate_mineshaft", () -> DeepslateMineshaftStructure.CODEC
    );
    public static final StructureType<NetherMineshaftStructure> NETHER_MINESHAFT = create(
            "nether_mineshaft", () -> NetherMineshaftStructure.CODEC
    );

    public static void register(BiConsumer<ResourceKey<StructureType<?>>, StructureType<?>> registrar) {
        STRUCTURE_TYPES.forEach(registrar);
    }

    private static <S extends Structure> StructureType<S> create(String name, StructureType<S> type) {
        STRUCTURE_TYPES.put(ResourceKey.create(Registries.STRUCTURE_TYPE, DeeperDelvings.id(name)), type);
        return type;
    }
}
