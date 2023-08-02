package me.croshaw.firetweaks.registry;

import me.croshaw.firetweaks.FireTweaks;
import me.croshaw.firetweaks.loot.LanternLootFunction;
import me.croshaw.firetweaks.loot.TorchLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.util.registry.Registry;

public class LootRegistry {
    public static final LootFunctionType FIXIT_TORCH_LOOT_FUNCTION = new LootFunctionType(new TorchLootFunction.Serializer());
    public static final LootFunctionType FIXIT_LANTERN_LOOT_FUNCTION = new LootFunctionType(new LanternLootFunction.Serializer());

    public static void registry() {
        Registry.register(Registry.LOOT_FUNCTION_TYPE, FireTweaks.id("torch"), FIXIT_TORCH_LOOT_FUNCTION);
        Registry.register(Registry.LOOT_FUNCTION_TYPE, FireTweaks.id("lantern"), FIXIT_LANTERN_LOOT_FUNCTION);
    }
}
