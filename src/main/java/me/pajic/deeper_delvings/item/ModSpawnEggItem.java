package me.pajic.deeper_delvings.item;

import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.SpawnEggItem;
import org.jetbrains.annotations.NotNull;

public class ModSpawnEggItem extends SpawnEggItem {

    private final boolean enabled;

    public ModSpawnEggItem(Properties properties, boolean enabled) {
        super(properties);
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled(@NotNull FeatureFlagSet featureFlagSet) {
        return enabled;
    }
}
