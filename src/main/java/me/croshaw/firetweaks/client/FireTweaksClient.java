package me.croshaw.firetweaks.client;

import me.croshaw.firetweaks.registry.ItemsRegistry;
import me.croshaw.firetweaks.util.FireTweaksProp;
import me.croshaw.firetweaks.util.StacksUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class FireTweaksClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FabricModelPredicateProviderRegistry.register(ItemsRegistry.TORCH_ITEM, new Identifier("state"), (stack, world, entity, seed) -> (float)StacksUtil.getOrDefault(stack, 0)/ FireTweaksProp.values().length);
        FabricModelPredicateProviderRegistry.register(ItemsRegistry.LANTERN_ITEM, new Identifier("state"), (stack, world, entity, seed) -> StacksUtil.isLit(stack) ? 1 : 0);
    }
}
