package me.croshaw.firetweaks.client;

import me.croshaw.firetweaks.registry.BlocksRegistry;
import me.croshaw.firetweaks.util.StacksUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class FireTweaksClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FabricModelPredicateProviderRegistry.register(BlocksRegistry.TORCH_ITEM, new Identifier("state"), (stack, world, entity, seed) -> StacksUtil.getOrDefault(stack, 0));
    }
}
