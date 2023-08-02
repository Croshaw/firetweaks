package me.croshaw.firetweaks.recipe;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class AccessableTagEntry implements Ingredient.Entry{
    private final TagKey<Item> tag;

    public AccessableTagEntry(TagKey<Item> tag) {
        this.tag = tag;
    }

    public Collection<ItemStack> getStacks() {
        List<ItemStack> list = Lists.newArrayList();
        Iterator var2 = Registry.ITEM.iterateEntries(this.tag).iterator();

        while(var2.hasNext()) {
            RegistryEntry<Item> registryEntry = (RegistryEntry)var2.next();
            list.add(new ItemStack(registryEntry));
        }

        return list;
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("tag", this.tag.id().toString());
        return jsonObject;
    }
}
