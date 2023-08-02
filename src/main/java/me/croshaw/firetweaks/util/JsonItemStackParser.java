package me.croshaw.firetweaks.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.JsonHelper;

public class JsonItemStackParser {
    public static void modifyNbtByJson(JsonObject json, ItemStack stack) {
        for (var nbtKey : json.keySet()) {
            JsonElement element = json.get(nbtKey);
            if(element.isJsonPrimitive()) {
                NbtCompound nbt = stack.getOrCreateNbt();
                JsonPrimitive primitive = element.getAsJsonPrimitive();
                if(primitive.isBoolean()) {
                    nbt.putBoolean(nbtKey, primitive.getAsBoolean());
                } else if(primitive.isNumber()) {
                    nbt.putInt(nbtKey, primitive.getAsInt());
                } else if(primitive.isString()) {
                    nbt.putString(nbtKey, primitive.getAsString());
                }
            }
        }
    }
}
