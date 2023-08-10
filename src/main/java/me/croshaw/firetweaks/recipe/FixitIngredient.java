package me.croshaw.firetweaks.recipe;

import com.google.gson.*;
import me.croshaw.firetweaks.util.JsonItemStackParser;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FixitIngredient {
    public static Ingredient fromJson(@Nullable JsonElement json) {
        if (json != null && !json.isJsonNull()) {
            if (json.isJsonObject()) {
                return Ingredient.ofEntries(Stream.of(entryFromJson(json.getAsJsonObject())));
            } else if (json.isJsonArray()) {
                JsonArray jsonArray = json.getAsJsonArray();
                if (jsonArray.size() == 0) {
                    throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
                } else {
                    return Ingredient.ofEntries(StreamSupport.stream(jsonArray.spliterator(), false).map((jsonElement) -> entryFromJson(JsonHelper.asObject(jsonElement, "item"))));
                }
            } else {
                throw new JsonSyntaxException("Expected item to be object or array of objects");
            }
        } else {
            throw new JsonSyntaxException("Item cannot be null");
        }
    }

    private static Ingredient.Entry entryFromJson(JsonObject json) {
        if (json.has("item") && json.has("tag")) {
            throw new JsonParseException("An ingredient entry is either a tag or an item, not both");
        } else if (json.has("item")) {
            Item item = ShapedRecipe.getItem(json);
            ItemStack stackEntry = new ItemStack(item);
            if(json.has("data")) {
                JsonItemStackParser.modifyNbtByJson(JsonHelper.getObject(json, "data"), stackEntry);
            }
            return new AccessableStackEntry(stackEntry);
        } else if (json.has("tag")) {
            Identifier identifier = new Identifier(JsonHelper.getString(json, "tag"));
            TagKey<Item> tagKey = TagKey.of(Registry.ITEM_KEY, identifier);
            return new AccessableTagEntry(tagKey);
        } else {
            throw new JsonParseException("An ingredient entry needs either a tag or an item");
        }
    }
}
