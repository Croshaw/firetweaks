package me.croshaw.firetweaks;

import me.croshaw.firetweaks.registry.ModRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class FireTweaks implements ModInitializer {
    public static final String MOD_ID = "firetweaks";
    public static final String NBT_ITEM_KEY = "burnable_state";

    @Override
    public void onInitialize() {
        ModRegistry.registry();
    }

    public static Identifier id(String name) {
        return new Identifier(MOD_ID, name);
    }
}
