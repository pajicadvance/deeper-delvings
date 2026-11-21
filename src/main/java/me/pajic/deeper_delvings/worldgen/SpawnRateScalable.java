package me.pajic.deeper_delvings.worldgen;

import java.util.function.Supplier;

public interface SpawnRateScalable {

    void deeper_delvings$setSpawnRateMultiplier(Supplier<Float> multiplier);
}
