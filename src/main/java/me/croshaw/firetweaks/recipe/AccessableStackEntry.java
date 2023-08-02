package me.croshaw.firetweaks.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.registry.Registry;

import java.util.Collection;
import java.util.Collections;

public class AccessableStackEntry implements Ingredient.Entry{
    private final ItemStack stack;

    public AccessableStackEntry(ItemStack stack) {
        this.stack = stack;
    }

    public Collection<ItemStack> getStacks() {
        return Collections.singleton(this.stack);
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("item", Registry.ITEM.getId(this.stack.getItem()).toString());
        NbtCompound nbt = stack.getNbt();
        if(nbt != null) {
            jsonObject.add("data", new JsonObject());
            for (String key : nbt.getKeys()) {
                NbtElement element = nbt.get(key);
                if(element instanceof NbtByte nbtByte) {
                    jsonObject.addProperty(key, nbtByte.byteValue()!=0);
                } else if(element instanceof AbstractNbtNumber nbtNumber) {
                    jsonObject.addProperty(key, nbtNumber.numberValue());
                } else if(element instanceof NbtString nbtString) {
                    jsonObject.addProperty(key, nbtString.asString());
                }
            }
        }
        return jsonObject;
    }
}
