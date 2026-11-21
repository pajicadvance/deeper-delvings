package me.pajic.deeper_delvings.entity;

import me.pajic.deeper_delvings.DeeperDelvings;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.SpiderEyesLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class FierySpiderRenderer extends SpiderRenderer<FierySpider> {

    private static final Identifier TEXTURE = DeeperDelvings.id("textures/entity/spider/fiery_spider.png");
    private static final RenderType GLOW = RenderTypes.eyes(DeeperDelvings.id("textures/entity/spider/fiery_spider_glow.png"));

    public FierySpiderRenderer(EntityRendererProvider.Context context) {
        super(context, ModelLayers.CAVE_SPIDER);
        shadowRadius = 0.56F;
        layers.removeIf(layer -> layer instanceof SpiderEyesLayer);
        addLayer(new EyesLayer<>(this) {
            @Override
            public @NotNull RenderType renderType() {
                return GLOW;
            }
        });
    }

    @Override
    public @NotNull Identifier getTextureLocation(@NotNull LivingEntityRenderState state) {
        return TEXTURE;
    }
}
