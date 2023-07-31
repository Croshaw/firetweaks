package me.croshaw.firetweaks.util;

import me.croshaw.firetweaks.FireTweaks;
import me.croshaw.firetweaks.registry.BlocksRegistry;
import net.minecraft.item.ItemStack;

public class StacksUtil {
    public static ItemStack createStack(FireTweaksProp burnableState) {
        ItemStack stack = new ItemStack(BlocksRegistry.TORCH_ITEM, 1);
        return modifyStack(stack, burnableState);
    }
    public static int getOrDefault(ItemStack stack, int def){
        try {
            return stack.getNbt().getInt(FireTweaks.NBT_ITEM_KEY);
        }
        catch (Exception exception) {
            return def;
        }
    }

    public static ItemStack modifyStack(ItemStack stack, FireTweaksProp burnableState) {
        switch (burnableState) {
            case UNLIT -> stack.getOrCreateNbt().putInt(FireTweaks.NBT_ITEM_KEY, 0);
            case LIT -> stack.getOrCreateNbt().putInt(FireTweaks.NBT_ITEM_KEY, 1);
            case SMOLDERING -> stack.getOrCreateNbt().putInt(FireTweaks.NBT_ITEM_KEY, 2);
            case BURNT -> stack.getOrCreateNbt().putInt(FireTweaks.NBT_ITEM_KEY, 3);
        }
        return stack;
    }

    public static FireTweaksProp getBlockStateFromStack(ItemStack stack) {
        return switch (getOrDefault(stack, 0)) {
            case 1 -> FireTweaksProp.LIT;
            case 2 -> FireTweaksProp.SMOLDERING;
            case 3 -> FireTweaksProp.BURNT;
            default -> FireTweaksProp.UNLIT;
        };
    }
}
