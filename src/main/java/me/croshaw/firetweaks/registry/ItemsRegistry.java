package me.croshaw.firetweaks.registry;

import me.croshaw.firetweaks.FireTweaks;
import me.croshaw.firetweaks.item.FireStarterItem;
import me.croshaw.firetweaks.item.FixitLanternBlockItem;
import me.croshaw.firetweaks.item.FixitWallStandingBlockItem;
import net.minecraft.util.registry.Registry;

public class ItemsRegistry {
    public static final FireStarterItem FIRE_STARTER_ITEM = new FireStarterItem(32);
    public final static FixitWallStandingBlockItem TORCH_ITEM = new FixitWallStandingBlockItem(BlocksRegistry.TORCH_BLOCK, BlocksRegistry.TORCH_WALL_BLOCK);
    public final static FixitLanternBlockItem LANTERN_ITEM = new FixitLanternBlockItem(BlocksRegistry.LANTERN_BLOCK, 1000);

    public static void registry() {
        Registry.register(Registry.ITEM, FireTweaks.id("fire_starter"), FIRE_STARTER_ITEM);
        Registry.register(Registry.ITEM, FireTweaks.id("torch"), ItemsRegistry.TORCH_ITEM);
        Registry.register(Registry.ITEM, FireTweaks.id("lantern"), ItemsRegistry.LANTERN_ITEM);
    }
}
