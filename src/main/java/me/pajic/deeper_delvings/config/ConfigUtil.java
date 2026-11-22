package me.pajic.deeper_delvings.config;

import me.fzzyhmstrs.fzzy_config.util.AllowableIdentifiers;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;
import net.minecraft.resources.Identifier;

import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Supplier;

public class ConfigUtil {

    public static ValidatedFloat steppedFloat(float defaultValue, float max, float min, float step, String format) {
        return ValidatedNumber.setFormat(
                ValidatedNumber.withIncrement(new ValidatedFloat(defaultValue, max, min, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS), step),
                new DecimalFormat(format)
        );
    }

    static ValidatedIdentifier suggestingIdentifier(Identifier defaultId, Supplier<List<Identifier>> suggestions) {
        return new ValidatedIdentifier(defaultId, new AllowableIdentifiers(id -> true, suggestions));
    }
}
