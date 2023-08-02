package me.croshaw.firetweaks.util;

import me.croshaw.firetweaks.FireTweaks;
import me.croshaw.firetweaks.registry.BlocksRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;

public class StacksUtil {

    public static boolean isLit(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if(nbt.contains(FireTweaks.NBT_LIT_KEY))
            return nbt.getBoolean(FireTweaks.NBT_LIT_KEY);
        else {
            nbt.putBoolean(FireTweaks.NBT_LIT_KEY, false);
            return isLit(stack);
        }
    }
    public static long getFuel(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if(nbt != null && nbt.contains(FireTweaks.NBT_FUEL_KEY))
            return nbt.getLong(FireTweaks.NBT_FUEL_KEY);
        return 0;
    }
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

    public static ItemStack modifyStack(ItemStack stack, int burnableState) {
        switch (burnableState) {
            case 0 -> stack.getOrCreateNbt().putInt(FireTweaks.NBT_ITEM_KEY, 0);
            case 1 -> stack.getOrCreateNbt().putInt(FireTweaks.NBT_ITEM_KEY, 1);
            case 2 -> stack.getOrCreateNbt().putInt(FireTweaks.NBT_ITEM_KEY, 2);
            case 3 -> stack.getOrCreateNbt().putInt(FireTweaks.NBT_ITEM_KEY, 3);
        }
        return stack;
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

    public static String getKey(Item item) {
        return Registry.ITEM.getKey(item).get().getValue().toString();
    }
}
