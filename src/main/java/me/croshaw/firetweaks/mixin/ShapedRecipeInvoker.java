package me.croshaw.firetweaks.mixin;

import com.google.gson.JsonArray;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(ShapedRecipe.class)
public interface ShapedRecipeInvoker {
    @Invoker("getPattern")
    static String[] getPattern(JsonArray json){
        throw new AssertionError();
    }

    @Invoker("removePadding")
    static String[] removePadding(String... pattern){
        throw new AssertionError();
    }
    @Invoker("createPatternMatrix")
    static DefaultedList<Ingredient> createPatternMatrix(String[] pattern, Map<String, Ingredient> symbols, int width, int height){
        throw new AssertionError();
    }
}
