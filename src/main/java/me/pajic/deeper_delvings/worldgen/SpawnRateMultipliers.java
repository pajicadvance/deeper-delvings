package me.pajic.deeper_delvings.worldgen;

import me.pajic.deeper_delvings.DeeperDelvings;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;
//? if >=26.3 {
import net.minecraft.world.level.levelgen.structure.placement.AbstractSpreadingStructurePlacement;
//?} else {
/*import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
*///?}

import java.util.Set;

public class SpawnRateMultipliers {

    private static final Set<Identifier> DUNGEON_FEATURES = Set.of(
            Identifier.withDefaultNamespace("monster_room"),
            Identifier.withDefaultNamespace("monster_room_deep")
    );
    private static final Set<Identifier> MINESHAFT_STRUCTURE_SETS = Set.of(
            Identifier.withDefaultNamespace("mineshafts"),
            DeeperDelvings.id("deepslate_mineshafts")
    );

    public static void onEntryRegistered(ResourceKey<?> key, Object value) {
        if (key.isFor(Registries.PLACED_FEATURE) && DUNGEON_FEATURES.contains(key.identifier())) {
            ((PlacedFeature) value).placement().forEach(modifier -> {
                if (modifier instanceof CountPlacement) {
                    ((SpawnRateScalable) modifier).deeper_delvings$setSpawnRateMultiplier(DeeperDelvings.CONFIG.worldgen.dungeonChanceMult);
                }
            });
        } else if (key.isFor(Registries.STRUCTURE_SET) && MINESHAFT_STRUCTURE_SETS.contains(key.identifier())) {
            //? if >=26.3 {
            if (((StructureSet) value).placement() instanceof AbstractSpreadingStructurePlacement placement) {
            //?} else {
            /*if (((StructureSet) value).placement() instanceof StructurePlacement placement) {
            *///?}
                ((SpawnRateScalable) placement).deeper_delvings$setSpawnRateMultiplier(DeeperDelvings.CONFIG.worldgen.mineshaftChanceMult);
            }
        }
    }

    public static int scaleCount(int count, float multiplier, RandomSource random) {
        double scaled = count * (double) multiplier;
        int whole = (int) scaled;
        double fraction = scaled - whole;
        return fraction > 0 && random.nextDouble() < fraction ? whole + 1 : whole;
    }
}
