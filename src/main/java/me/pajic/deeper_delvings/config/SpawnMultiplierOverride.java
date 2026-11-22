package me.pajic.deeper_delvings.config;

import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.util.Walkable;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;

@Translation(prefix = "deeper_delvings.config.spawnMultiplierOverride")
public class SpawnMultiplierOverride implements Walkable {

    public ValidatedFloat multiplier = ConfigUtil.steppedFloat(1F, 2F, 0.1F, 0.05F, "0.##");
}
