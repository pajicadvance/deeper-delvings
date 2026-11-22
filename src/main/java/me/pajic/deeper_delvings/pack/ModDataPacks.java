package me.pajic.deeper_delvings.pack;

import me.pajic.deeper_delvings.DeeperDelvings;
import me.pajic.deeper_delvings.ModFeatures;

import java.util.ArrayList;
import java.util.List;

public class ModDataPacks {

    public static List<BuiltinPack> getPacks() {
        List<BuiltinPack> packs = new ArrayList<>();
        if (ModFeatures.mossyDeepslateSet()) {
            packs.add(new BuiltinPack("mossy_deepslate", "Mossy deepslate"));
        }
        if (DeeperDelvings.CONFIG.deepslateMineshaftVariant.get()) {
            packs.add(new BuiltinPack("deepslate_mineshafts", "Deepslate mineshafts"));
        }
        if (DeeperDelvings.CONFIG.netherMineshafts.get()) {
            packs.add(new BuiltinPack("nether_mineshafts", "Nether mineshafts"));
        }
        return packs;
    }

    public record BuiltinPack(String id, String name) {}
}
