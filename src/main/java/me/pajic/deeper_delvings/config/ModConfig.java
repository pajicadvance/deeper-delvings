package me.pajic.deeper_delvings.config;

import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.annotations.Version;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedAny;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;
import me.pajic.deeper_delvings.DeeperDelvings;
import me.pajic.deeper_delvings.worldgen.ore.OreSizeScaling;
import me.pajic.deeper_delvings.worldgen.spawnrate.SpawnRateMultipliers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;

@SuppressWarnings({"unchecked", "rawtypes"})
@Version(version = 1)
public class ModConfig extends Config {

	public ModConfig() {
		super(DeeperDelvings.id("config"));
	}

    @RequiresAction(action = Action.RESTART)
    public ValidatedBoolean deepslateMineshaftVariant = new ValidatedBoolean();
    @RequiresAction(action = Action.RESTART)
    public ValidatedBoolean netherMineshafts = new ValidatedBoolean();
    @RequiresAction(action = Action.RESTART)
    public ValidatedBoolean deepslateDungeonVariant = new ValidatedBoolean();
    public BonusChests bonusChests = new BonusChests();
	public OreSizeMultipliers oreSizeMultipliers = new OreSizeMultipliers();
	public StructureSpawnMultipliers structureSpawnMultipliers = new StructureSpawnMultipliers();
	public ModIntegration modIntegration = new ModIntegration();
	public Misc misc = new Misc();

    public static class BonusChests extends ConfigSection {
        public ValidatedBoolean mansionChests = new ValidatedBoolean(false);
        public ValidatedFloat mansionChance = ConfigUtil.steppedFloat(0.0039F, 0.01F, 0F, 0.0001F, "0.####");
        public ValidatedBoolean monumentChests = new ValidatedBoolean(false);
        public ValidatedFloat monumentChance = ConfigUtil.steppedFloat(0.0025F, 0.01F, 0F, 0.0001F, "0.####");
    }

	public static class OreSizeMultipliers extends ConfigSection {
        public ValidatedFloat minMult = ConfigUtil.steppedFloat(1F, 2F, 0.1F, 0.05F, "0.##");
        public ValidatedFloat maxMult = ConfigUtil.steppedFloat(1F, 2F, 0.1F, 0.05F, "0.##");
        public ValidatedInt multMaxY = new ValidatedInt(192, 384, -64);
        public ValidatedInt multMidY = new ValidatedInt(96, 384, -64);
        public ValidatedInt multMinY = new ValidatedInt(0, 384, -64);
        public ValidatedMap<Identifier, OreSizeOverride> overrides = (new ValidatedMap.Builder())
                .keyHandler(ConfigUtil.suggestingIdentifier(Identifier.withDefaultNamespace("iron_ore"), OreSizeScaling::suggestions))
                .valueHandler(new ValidatedAny<>(new OreSizeOverride()))
                .build();
	}

	public static class StructureSpawnMultipliers extends ConfigSection {
        public ValidatedFloat dungeonChanceMult = ConfigUtil.steppedFloat(1F, 2F, 0.1F, 0.05F, "0.##");
        public ValidatedMap<Identifier, SpawnMultiplierOverride> dungeonChanceOverrides = (new ValidatedMap.Builder())
                .keyHandler(ConfigUtil.suggestingIdentifier(Identifier.withDefaultNamespace("monster_room"), ValidatedIdentifier.ofDynamicKey(
                        Registries.PLACED_FEATURE, "deeper_delvings_dungeon_variants", (id, feature) -> SpawnRateMultipliers.isDungeon(feature.value())
                ).getAllowableIds()::get))
                .valueHandler(new ValidatedAny<>(new SpawnMultiplierOverride()))
                .build();
        public ValidatedFloat mineshaftChanceMult = ConfigUtil.steppedFloat(1F, 2F, 0.1F, 0.05F, "0.##");
        public ValidatedMap<Identifier, SpawnMultiplierOverride> mineshaftChanceOverrides = (new ValidatedMap.Builder())
                .keyHandler(ConfigUtil.suggestingIdentifier(Identifier.withDefaultNamespace("mineshafts"), ValidatedIdentifier.ofDynamicKey(
                        Registries.STRUCTURE_SET, "deeper_delvings_mineshaft_variants", (id, set) -> SpawnRateMultipliers.isMineshaftSet(set.value())
                ).getAllowableIds()::get))
                .valueHandler(new ValidatedAny<>(new SpawnMultiplierOverride()))
                .build();
	}

	public static class ModIntegration extends ConfigSection {
		@RequiresAction(action = Action.RESTART)
		public ValidatedList<Identifier> fierySpiderBiomes = ConfigUtil.suggestingIdentifier(
				Identifier.fromNamespaceAndPath("clifftree", "inferno"),
				ValidatedIdentifier.ofDynamicKey(Registries.BIOME, "deeper_delvings_biomes", (id, biome) -> true).getAllowableIds()::get
		).toList(Identifier.fromNamespaceAndPath("clifftree", "inferno"));
	}

	public static class Misc extends ConfigSection {
		public ValidatedInt caveAmbientSoundFrequency = new ValidatedInt(6000, 12000, 200, ValidatedNumber.WidgetType.TEXTBOX);
	}
}
