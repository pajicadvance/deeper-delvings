package me.pajic.deeper_delvings.worldgen.ore;

import me.pajic.deeper_delvings.DeeperDelvings;
import me.pajic.deeper_delvings.config.ModConfig;
import me.pajic.deeper_delvings.config.OreSizeOverride;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class OreSizeScaling {

    private static final TagKey<Block> ORES = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ores"));

    public static List<Identifier> suggestions() {
        return BuiltInRegistries.BLOCK.listElements()
                .filter(holder -> holder.is(ORES) || holder.key().identifier().getPath().endsWith("_ore"))
                .map(holder -> holder.key().identifier())
                .sorted()
                .toList();
    }

    public static @Nullable Float getMultiplier(List<BlockState> targets, int y) {
        Map<Identifier, ? extends OreSizeOverride> overrides = DeeperDelvings.CONFIG.oreSizeMultipliers.overrides.get();
        for (BlockState target : targets) {
            OreSizeOverride override = overrides.get(BuiltInRegistries.BLOCK.getKey(target.getBlock()));
            if (override != null) return getMultiplier(
                    y,
                    override.minMult.get(),
                    override.maxMult.get(),
                    override.multMaxY.get(),
                    override.multMidY.get(),
                    override.multMinY.get()
            );
        }
        if (targets.stream().noneMatch(target -> target.is(ORES))) return null;
        ModConfig.OreSizeMultipliers config = DeeperDelvings.CONFIG.oreSizeMultipliers;
        return getMultiplier(
                y,
                config.minMult.get(),
                config.maxMult.get(),
                config.multMaxY.get(),
                config.multMidY.get(),
                config.multMinY.get()
        );
    }

    private static float getMultiplier(int y, float minMult, float maxMult, int maxY, int midY, int minY) {
        if (y >= maxY) return minMult;
        if (y <= minY) return maxMult;
        if (y >= midY) return Mth.lerp((float) (y - midY) / (maxY - midY), 1.0F, minMult);
        return Mth.lerp((float) (midY - y) / (midY - minY), 1.0F, maxMult);
    }
}
