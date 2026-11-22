package me.pajic.deeper_delvings.config;

import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.util.Walkable;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;

@Translation(prefix = "deeper_delvings.config.oreSizeOverride")
public class OreSizeOverride implements Walkable {

    public ValidatedFloat minMult = ConfigUtil.steppedFloat(1F, 2F, 0.1F, 0.05F, "0.##");
    public ValidatedFloat maxMult = ConfigUtil.steppedFloat(1F, 2F, 0.1F, 0.05F, "0.##");
    public ValidatedInt multMaxY = new ValidatedInt(192, 384, -64);
    public ValidatedInt multMidY = new ValidatedInt(96, 384, -64);
    public ValidatedInt multMinY = new ValidatedInt(0, 384, -64);
}
