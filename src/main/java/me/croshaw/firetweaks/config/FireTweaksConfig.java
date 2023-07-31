package me.croshaw.firetweaks.config;

import net.minecraft.enchantment.Enchantments;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

import java.util.List;

public class FireTweaksConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final CommonConfig COMMON = new CommonConfig(BUILDER);
    public static final ForgeConfigSpec SPEC = BUILDER.build();
    public static Integer getHitDuration() {
        return COMMON.hitDuration.get();
    }
    public static List<? extends String> getExtraTorchItems() {
        return COMMON.extraTorchItems.get();
    }
    public static List<? extends String> getExtraSoulTorchItems() {
        return COMMON.extraSoulTorchItems.get();
    }
    public static List<? extends String> getExtraFlammableItems() {
        return COMMON.extraFlammableItems.get();
    }
    public static Boolean getVanillaTorchesEnabled() {
        return COMMON.vanillaTorchesEnabled.get();
    }
    public static Boolean getAllowCandles() {
        return COMMON.allowCandles.get();
    }
    public static Boolean getAllowFlammableItems() {
        return COMMON.allowFlammableItems.get();
    }
    public static Boolean getConsumeCandle() {
        return COMMON.consumeCandle.get();
    }
    public static Boolean getConsumeTorch() {
        return COMMON.consumeTorch.get();
    }
    public static Boolean getConsumeFlammableItems() {
        return COMMON.consumeFlammableItems.get();
    }
    public static Boolean getConsumeWithoutFire() {
        return COMMON.consumeWithoutFire.get();
    }
    public static Integer getFireChanceByAllowItems() {
        return COMMON.fireChanceByAllowItems.get();
    }
    public static Boolean getAllowHitByBurnEntity() {
        return COMMON.allowHitByBurnEntity.get();
    }
    public static Integer getFireChanceWhenBurn() {
        return COMMON.fireChanceWhenBurn.get();
    }

    //Torch Options
    public static Integer getLitTorchFuel() {
        return COMMON.litTorchFuel.get();
    }
    public static Integer getSmolderingTorchFuel() {
        return COMMON.smolderingTorchFuel.get();
    }
    public static Double getChanceExtinguishByRain() {
        return COMMON.chanceExtinguishByRain.get();
    }

    public static class CommonConfig {
        private final IntValue hitDuration;
        private final ConfigValue<List<? extends String>> extraTorchItems;
        private final ConfigValue<List<? extends String>> extraSoulTorchItems;
        private final ConfigValue<List<? extends String>> extraFlammableItems;
        private final BooleanValue vanillaTorchesEnabled;
        private final ConfigValue<Boolean> allowCandles;
        private final ConfigValue<Boolean> allowFlammableItems;
        private final ConfigValue<Boolean> consumeCandle;
        private final BooleanValue consumeTorch;
        private final BooleanValue consumeFlammableItems;
        private final BooleanValue consumeWithoutFire;
        private final IntValue fireChanceByAllowItems;
        private final BooleanValue allowHitByBurnEntity;
        private final IntValue fireChanceWhenBurn;

        //Torch Options
        private final IntValue litTorchFuel;
        private final IntValue smolderingTorchFuel;
        private final DoubleValue chanceExtinguishByRain;

        public CommonConfig(ForgeConfigSpec.Builder builder) {
            int maxDuration = Enchantments.FIRE_ASPECT.getMaxLevel() * 4;
            hitDuration = builder.comment("Fire damage duration for direct (main hand) hits.").defineInRange("hit duration", 4, 1, maxDuration);
            extraTorchItems = builder.comment("List of item ids that should be considered as a Torch.").defineListAllowEmpty(
                    List.of("extra torch items"),
                    () -> List.of(
                            "bonetorch:bonetorch",
                            "torchmaster:megatorch",
                            "hardcore_torches:lit_torch",
                            "magnumtorch:diamond_magnum_torch",
                            "magnumtorch:emerald_magnum_torch",
                            "magnumtorch:amethyst_magnum_torch",
                            "magical_torches:mega_torch",
                            "magical_torches:grand_torch",
                            "magical_torches:medium_torch",
                            "magical_torches:small_torch",
                            "pgwbandedtorches:banded_torch_white",
                            "pgwbandedtorches:banded_torch_orange",
                            "pgwbandedtorches:banded_torch_magenta",
                            "pgwbandedtorches:banded_torch_light_blue",
                            "pgwbandedtorches:banded_torch_yellow",
                            "pgwbandedtorches:banded_torch_lime",
                            "pgwbandedtorches:banded_torch_pink",
                            "pgwbandedtorches:banded_torch_gray",
                            "pgwbandedtorches:banded_torch_light_gray",
                            "pgwbandedtorches:banded_torch_cyan",
                            "pgwbandedtorches:banded_torch_purple",
                            "pgwbandedtorches:banded_torch_blue",
                            "pgwbandedtorches:banded_torch_brown",
                            "pgwbandedtorches:banded_torch_green",
                            "pgwbandedtorches:banded_torch_red",
                            "pgwbandedtorches:banded_torch_black"
                    ),
                    (element) -> element instanceof String && !((String) element).isBlank()
            );
            extraSoulTorchItems = builder.comment("List of item ids that should be considered as a Soul Torch.").defineListAllowEmpty(
                    List.of("extra soul torch items"),
                    () -> List.of(
                            "pgwbandedtorches:banded_soul_torch_white",
                            "pgwbandedtorches:banded_soul_torch_orange",
                            "pgwbandedtorches:banded_soul_torch_magenta",
                            "pgwbandedtorches:banded_soul_torch_light_blue",
                            "pgwbandedtorches:banded_soul_torch_yellow",
                            "pgwbandedtorches:banded_soul_torch_lime",
                            "pgwbandedtorches:banded_soul_torch_pink",
                            "pgwbandedtorches:banded_soul_torch_gray",
                            "pgwbandedtorches:banded_soul_torch_light_gray",
                            "pgwbandedtorches:banded_soul_torch_cyan",
                            "pgwbandedtorches:banded_soul_torch_purple",
                            "pgwbandedtorches:banded_soul_torch_blue",
                            "pgwbandedtorches:banded_soul_torch_brown",
                            "pgwbandedtorches:banded_soul_torch_green",
                            "pgwbandedtorches:banded_soul_torch_red",
                            "pgwbandedtorches:banded_soul_torch_black"
                    ),
                    (element) -> element instanceof String && !((String) element).isBlank()
            );
            extraFlammableItems = builder.comment("EMPTY").defineListAllowEmpty(
                    List.of("extra burnable items"),
                    () -> List.of(
                            "minecraft:flint_and_steel"
                    ),
                    (element) -> element instanceof String && !((String) element).isBlank()
            );
            vanillaTorchesEnabled = builder
                    .comment(
                            "Whether Vanilla torches can set targets on fire.",
                            "If false, only the items specified by [extra torch items] and [extra soul torch items] will set targets on fire."
                    )
                    .define("vanilla torches enabled", true);
            allowCandles = builder.comment("Whether to allow candles to act as torches.").define("allow candles", true);
            allowFlammableItems = builder.comment("EMPTY").define("allow burnable items", true);
            consumeCandle = builder.comment("Whether candles should break upon use.", "Effective only if [allowCandles] is enabled.").define("consume candle", true);
            consumeTorch = builder.comment("Whether torches should break upon use.").define("consume torch", false);
            consumeFlammableItems = builder.comment("EMPTY").define("consume flammable items", false);
            consumeWithoutFire = builder
                    .comment(
                            "Whether to break the torch/candle upon use even if no fire was set.",
                            "Effective only if [fire chance] and at least one of [consume torch] and [consume candle] are set different from default."
                    )
                    .define("consume without fire", false);
            fireChanceByAllowItems = builder.comment("Chance (in percentage) for torches/candles to set targets on fire.").defineInRange("fire Chance By Allow Items", 100, 1, 100);
            allowHitByBurnEntity = builder.comment("EMPTY").define("allow Hit By Burn Entity", true);
            fireChanceWhenBurn = builder.comment("EMPTY").defineInRange("fire Chance When Burn", 100, 1, 100);
            litTorchFuel = builder.comment("EMPTY").defineInRange("lit torch fuel", 600, 60, 999999999);
            smolderingTorchFuel = builder.comment("EMPTY").defineInRange("lit torch fuel", 60, 5, 999999999);
            chanceExtinguishByRain = builder.comment("EMPTY").defineInRange("chance Extinguish By Rain", 1d, 0d, 100d);
        }
    }
}