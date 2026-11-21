package me.pajic.deeper_delvings.platform.fabric;

//? fabric {

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import me.pajic.deeper_delvings.DeeperDelvings;
import me.pajic.deeper_delvings.entity.FierySpiderRenderer;
import me.pajic.deeper_delvings.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EntityRenderers.register(ModEntities.FIERY_SPIDER, FierySpiderRenderer::new);
		DeeperDelvings.onInitializeClient();
	}
}
//?}
