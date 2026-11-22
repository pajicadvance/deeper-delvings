package me.pajic.deeper_delvings.worldgen.spawnrate;

import com.google.common.base.Suppliers;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap;
import me.pajic.deeper_delvings.DeeperDelvings;
import me.pajic.deeper_delvings.config.SpawnMultiplierOverride;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.MonsterRoomFeature;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
//? if >=26.3 {
import net.minecraft.world.level.levelgen.structure.placement.AbstractSpreadingStructurePlacement;
//?} else {
/*import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
*///?}

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class SpawnRateMultipliers {

    public static boolean isDungeon(PlacedFeature feature) {
        //? if >=26.3 {
        return feature.feature().value() instanceof MonsterRoomFeature;
        //?} else {
        /*return feature.feature().value().feature() instanceof MonsterRoomFeature;
        *///?}
    }

    public static boolean isMineshaftSet(StructureSet structureSet) {
        return structureSet.structures().stream().anyMatch(entry -> entry.structure().value() instanceof MineshaftStructure);
    }

    public static void onEntryRegistered(ResourceKey<?> key, Object value) {
        Identifier id = key.identifier();
        if (key.isFor(Registries.PLACED_FEATURE)) {
            PlacedFeature feature = (PlacedFeature) value;
            Supplier<Float> multiplier = multiplier(
                    id,
                    () -> isDungeon(feature),
                    DeeperDelvings.CONFIG.structureSpawnMultipliers.dungeonChanceOverrides,
                    DeeperDelvings.CONFIG.structureSpawnMultipliers.dungeonChanceMult
            );
            feature.placement().forEach(modifier -> {
                if (modifier instanceof CountPlacement) {
                    ((SpawnRateScalable) modifier).deeper_delvings$setSpawnRateMultiplier(multiplier);
                }
            });
        } else if (key.isFor(Registries.STRUCTURE_SET)) {
            StructureSet structureSet = (StructureSet) value;
            //? if >=26.3 {
            if (structureSet.placement() instanceof AbstractSpreadingStructurePlacement placement) {
            //?} else {
            /*if (structureSet.placement() instanceof StructurePlacement placement) {
            *///?}
                ((SpawnRateScalable) placement).deeper_delvings$setSpawnRateMultiplier(multiplier(
                        id,
                        () -> isMineshaftSet(structureSet),
                        DeeperDelvings.CONFIG.structureSpawnMultipliers.mineshaftChanceOverrides,
                        DeeperDelvings.CONFIG.structureSpawnMultipliers.mineshaftChanceMult
                ));
            }
        }
    }

    private static Supplier<Float> multiplier(Identifier id, BooleanSupplier isVariant, ValidatedMap<Identifier, SpawnMultiplierOverride> overrides, Supplier<Float> global) {
        Supplier<Boolean> variant = Suppliers.memoize(isVariant::getAsBoolean);
        return () -> {
            SpawnMultiplierOverride override = overrides.get().get(id);
            if (override != null) return override.multiplier.get();
            return variant.get() ? global.get() : 1F;
        };
    }

    public static int scaleCount(int count, float multiplier, RandomSource random) {
        double scaled = count * (double) multiplier;
        int whole = (int) scaled;
        double fraction = scaled - whole;
        return fraction > 0 && random.nextDouble() < fraction ? whole + 1 : whole;
    }
}
