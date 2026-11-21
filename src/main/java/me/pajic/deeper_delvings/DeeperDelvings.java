package me.pajic.deeper_delvings;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.pajic.deeper_delvings.config.ModConfig;
import me.pajic.deeper_delvings.platform.MultiLoaderUtil;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeeperDelvings {

    public static final String MOD_ID = /*$ mod_id*/ "deeper_delvings";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ModConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ModConfig::new);

    public static void onInitialize() {
    }

    public static void onInitializeClient() {
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void debugLog(String message, Object ... args) {
        if (MultiLoaderUtil.INSTANCE.isDevEnv()) LOGGER.info(message, args);
    }
}
