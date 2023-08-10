package me.croshaw.firetweaks.recipe;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import me.croshaw.firetweaks.mixin.ShapedRecipeInvoker;
import me.croshaw.firetweaks.registry.RecipesRegistry;
import me.croshaw.firetweaks.util.JsonItemStackParser;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class FixitShapedRecipe extends ShapedRecipe {

    public FixitShapedRecipe(Identifier id, String group, int width, int height, DefaultedList<Ingredient> input, ItemStack output) {
        super(id, group, width, height, input, output);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipesRegistry.FIXIT_SHAPED_RECIPE_SERIALIZER;
    }

    @Override
    public boolean matches(CraftingInventory craftingInventory, World world) {
        for(int i = 0; i <= craftingInventory.getWidth() - this.getWidth(); ++i) {
            for(int j = 0; j <= craftingInventory.getHeight() - this.getHeight(); ++j) {
                if (this.matchesPattern(craftingInventory, i, j, true)) {
                    return true;
                }

                if (this.matchesPattern(craftingInventory, i, j, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean matchesPattern(CraftingInventory inv, int offsetX, int offsetY, boolean flipped) {
        for(int i = 0; i < inv.getWidth(); ++i) {
            for(int j = 0; j < inv.getHeight(); ++j) {
                int k = i - offsetX;
                int l = j - offsetY;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.getWidth() && l < this.getHeight()) {
                    if (flipped) {
                        ingredient = (Ingredient)this.getIngredients().get(this.getWidth() - k - 1 + l * this.getWidth());
                    } else {
                        ingredient = (Ingredient)this.getIngredients().get(k + l * this.getWidth());
                    }
                }

                if (!ingredient.test(inv.getStack(i + j * inv.getWidth()))) {
                    return false;
                }
            }
        }

        return true;
    }

    static Map<String, Ingredient> readSymbols(JsonObject json) {
        Map<String, Ingredient> map = Maps.newHashMap();
        Iterator var2 = json.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<String, JsonElement> entry = (Map.Entry)var2.next();
            if (((String)entry.getKey()).length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + (String)entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            map.put((String)entry.getKey(), FixitIngredient.fromJson((JsonElement)entry.getValue()));
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    public static ItemStack outputFromJson(JsonObject json) {
        Item item = getItem(json);
        int i = JsonHelper.getInt(json, "count", 1);
        if (i < 1) {
            throw new JsonSyntaxException("Invalid output count: " + i);
        } else {
            ItemStack stack = new ItemStack(item, i);
            if(json.has("data")) {
                JsonItemStackParser.modifyNbtByJson(JsonHelper.getObject(json, "data"), stack);
            }
            return stack;
        }
    }

    public static class Serializer implements RecipeSerializer<FixitShapedRecipe> {

        @Override
        public FixitShapedRecipe read(Identifier id, JsonObject json) {
            String string = JsonHelper.getString(json, "group", "");
            Map<String, Ingredient> map = FixitShapedRecipe.readSymbols(JsonHelper.getObject(json, "key"));
            String[] strings = ShapedRecipeInvoker.removePadding(ShapedRecipeInvoker.getPattern(JsonHelper.getArray(json, "pattern")));
            int i = strings[0].length();
            int j = strings.length;
            DefaultedList<Ingredient> defaultedList = ShapedRecipeInvoker.createPatternMatrix(strings, map, i, j);
            ItemStack itemStack = FixitShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
            return new FixitShapedRecipe(id, string, i, j, defaultedList, itemStack);
        }

        @Override
        public FixitShapedRecipe read(Identifier id, PacketByteBuf buf) {
            ShapedRecipe recipe = RecipeSerializer.SHAPED.read(id, buf);
            return new FixitShapedRecipe(recipe.getId(), recipe.getGroup(), recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), recipe.getOutput());
        }

        @Override
        public void write(PacketByteBuf buf, FixitShapedRecipe recipe) {
            RecipeSerializer.SHAPED.write(buf, recipe);
        }
    }
}
