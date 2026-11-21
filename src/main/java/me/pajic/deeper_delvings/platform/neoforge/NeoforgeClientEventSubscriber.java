package me.pajic.deeper_delvings.platform.neoforge;

//? neoforge {

/*import me.pajic.deeper_delvings.DeeperDelvings;
import me.pajic.deeper_delvings.entity.FierySpiderRenderer;
import me.pajic.deeper_delvings.entity.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = DeeperDelvings.MOD_ID, value = Dist.CLIENT)
public class NeoforgeClientEventSubscriber {

	@SubscribeEvent
	public static void onClientSetup(final FMLCommonSetupEvent event) {
        DeeperDelvings.onInitializeClient();
	}

	@SubscribeEvent
	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntities.FIERY_SPIDER, FierySpiderRenderer::new);
	}
}
*///?}
