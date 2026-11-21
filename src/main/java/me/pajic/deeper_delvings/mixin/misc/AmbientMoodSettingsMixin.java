package me.pajic.deeper_delvings.mixin.misc;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.pajic.deeper_delvings.DeeperDelvings;
import net.minecraft.world.attribute.AmbientMoodSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AmbientMoodSettings.class)
public class AmbientMoodSettingsMixin {

    @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=6000"
            )
    )
    private static int modifyCaveAmbientSoundFrequency(int original) {
        return DeeperDelvings.CONFIG.misc.caveAmbientSoundFrequency.get();
    }
}
