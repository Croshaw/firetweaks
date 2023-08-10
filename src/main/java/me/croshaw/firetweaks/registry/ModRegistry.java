package me.croshaw.firetweaks.registry;

import me.croshaw.firetweaks.FireTweaks;
import me.croshaw.firetweaks.config.FireTweaksConfig;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ModRegistry {

    public static void registry() {
        ModLoadingContext.registerConfig(FireTweaks.MOD_ID, ModConfig.Type.COMMON, FireTweaksConfig.SPEC);
        EventsRegistry.registry();
        BlocksRegistry.registry();
        ItemsRegistry.registry();
        LootRegistry.registry();
        RecipesRegistry.registry();
    }
}
