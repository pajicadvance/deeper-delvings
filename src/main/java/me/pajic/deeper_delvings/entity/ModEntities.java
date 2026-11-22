package me.pajic.deeper_delvings.entity;

import me.pajic.deeper_delvings.DeeperDelvings;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ModEntities {

    private static final Map<ResourceKey<EntityType<?>>, EntityType<?>> ENTITY_TYPES = new LinkedHashMap<>();

    public static final EntityType<FierySpider> FIERY_SPIDER = create(
            "fiery_spider",
            EntityType.Builder.of(FierySpider::new, MobCategory.MONSTER)
                    .sized(0.7F, 0.5F)
                    .eyeHeight(0.45F)
                    .clientTrackingRange(8)
                    .notInPeaceful()
                    .fireImmune()
    );

    public static void register(BiConsumer<ResourceKey<EntityType<?>>, EntityType<?>> registrar) {
        ENTITY_TYPES.forEach(registrar);
    }

    private static <T extends Entity> EntityType<T> create(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, DeeperDelvings.id(name));
        EntityType<T> type = builder.build(key);
        ENTITY_TYPES.put(key, type);
        return type;
    }
}
