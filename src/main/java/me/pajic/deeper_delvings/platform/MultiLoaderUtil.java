package me.pajic.deeper_delvings.platform;

//$ loader_util_import
import me.pajic.deeper_delvings.platform.fabric.FabricLoaderUtil;

public interface MultiLoaderUtil {
    MultiLoaderUtil INSTANCE = /*$ loader_util_inst*/ new FabricLoaderUtil();

    boolean isModLoaded(String modId);
    boolean isDevEnv();
}
