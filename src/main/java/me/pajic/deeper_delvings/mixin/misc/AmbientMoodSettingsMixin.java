package me.pajic.deeper_delvings.mixin.misc;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.pajic.deeper_delvings.DeeperDelvings;
import net.minecraft.world.attribute.AmbientMoodSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AmbientMoodSettings.class)
public class AmbientMoodSettingsMixin {

    @ModifyReturnValue(
            method = "tickDelay",
            at = @At("RETURN")
    )
    private int modifyCaveAmbientSoundFrequency(int original) {
        return (Object) this == AmbientMoodSettings.LEGACY_CAVE_SETTINGS ? DeeperDelvings.CONFIG.misc.caveAmbientSoundFrequency.get() : original;
    }
}
