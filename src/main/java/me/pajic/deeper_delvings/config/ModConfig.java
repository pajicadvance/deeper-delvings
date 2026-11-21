package me.pajic.deeper_delvings.config;

import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.annotations.Version;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;
import me.pajic.deeper_delvings.DeeperDelvings;

@Version(version = 1)
public class ModConfig extends Config {

	public ModConfig() {
		super(DeeperDelvings.id("config"));
	}

	public Content content = new Content();
	public Worldgen worldgen = new Worldgen();
	public Misc misc = new Misc();

	public static class Content extends ConfigSection {
		@RequiresAction(action = Action.RESTART)
		public ValidatedBoolean mossyDeepslateSet = new ValidatedBoolean(true);
		@RequiresAction(action = Action.RESTART)
		public ValidatedBoolean fierySpider = new ValidatedBoolean(true);
	}

	public static class Worldgen extends ConfigSection {
		public ValidatedFloat dungeonChanceMult = new ValidatedFloat(1.3F);
		public ValidatedFloat mineshaftChanceMult = new ValidatedFloat(1.2F);
		public ValidatedFloat oreSizeMinMult = new ValidatedFloat(0.8F);
		public ValidatedFloat oreSizeMaxMult = new ValidatedFloat(1.2F);
		public ValidatedInt oreSizeMultMaxY = new ValidatedInt(192);
		public ValidatedInt oreSizeMultMidY = new ValidatedInt(96);
		public ValidatedInt oreSizeMultMinY = new ValidatedInt(0);
		public ValidatedBoolean deepslateDungeonVariant = new ValidatedBoolean(true);
		@RequiresAction(action = Action.RESTART)
		public ValidatedBoolean deepslateMineshaftVariant = new ValidatedBoolean(true);
		@RequiresAction(action = Action.RESTART)
		public ValidatedBoolean netherMineshafts = new ValidatedBoolean(true);
		public ValidatedBoolean addRandomChestsToMansions = new ValidatedBoolean(true);
		public ValidatedFloat mansionRandomChestChance = new ValidatedFloat(0.0039F, 1.0F, 0F, ValidatedNumber.WidgetType.TEXTBOX);
		public ValidatedBoolean addRandomChestsToMonuments = new ValidatedBoolean(true);
		public ValidatedFloat monumentRandomChestChance = new ValidatedFloat(0.0025F, 1.0F, 0F, ValidatedNumber.WidgetType.TEXTBOX);
	}

	public static class Misc extends ConfigSection {
		@RequiresAction(action = Action.RESTART)
		public ValidatedInt caveAmbientSoundFrequency = new ValidatedInt(800, Integer.MAX_VALUE, 0);
	}
}
