package me.pajic.deeper_delvings.mixin.worldgen;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MineshaftStructure.Type.class)
public class MineshaftStructureTypeByIdMixin {

    @WrapMethod(method = "byId")
    private static MineshaftStructure.Type resolveAddedTypes(int id, Operation<MineshaftStructure.Type> original) {
        MineshaftStructure.Type[] values = MineshaftStructure.Type.values();
        return id >= 0 && id < values.length ? values[id] : original.call(id);
    }
}
