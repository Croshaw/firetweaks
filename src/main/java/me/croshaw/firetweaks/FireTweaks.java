package me.croshaw.firetweaks;

import me.croshaw.firetweaks.config.FireTweaksConfig;
import me.croshaw.firetweaks.event.AttackEntityCallback;
import me.croshaw.firetweaks.handlers.AttackEntityHandler;
import me.croshaw.firetweaks.handlers.ServerTickHandler;
import me.croshaw.firetweaks.registry.ModRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class FireTweaks implements ModInitializer {
    public static final String MOD_ID = "firetweaks";
    @Override
    public void onInitialize() {
        ModLoadingContext.registerConfig(MOD_ID, ModConfig.Type.COMMON, FireTweaksConfig.SPEC);
        ModRegistry.registry();
        AttackEntityCallback.EVENT.register(AttackEntityHandler::handle);
        ServerTickEvents.END_WORLD_TICK.register(ServerTickHandler::handle);
    }

    public static Identifier id(String name) {
        return new Identifier(MOD_ID, name);
    }
}
