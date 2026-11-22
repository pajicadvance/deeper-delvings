package me.pajic.deeper_delvings;

public class ModFeatures {

    public static boolean mossyDeepslateSet() {
        return DeeperDelvings.CONFIG.deepslateDungeonVariant.get();
    }

    public static boolean fierySpider() {
        return DeeperDelvings.CONFIG.netherMineshafts.get() || !DeeperDelvings.CONFIG.modIntegration.fierySpiderBiomes.get().isEmpty();
    }
}
