package me.croshaw.firetweaks.registry;

import me.croshaw.firetweaks.FireTweaks;
import me.croshaw.firetweaks.recipe.FixitShapedRecipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.registry.Registry;

public class RecipesRegistry {

    public static final RecipeType<FixitShapedRecipe> FIXIT_SHAPED_RECIPE = RecipeType.register(FireTweaks.id("crafting_shaped").toString());
    public static RecipeSerializer<FixitShapedRecipe> FIXIT_SHAPED_RECIPE_SERIALIZER;

    public static void registry() {
        FIXIT_SHAPED_RECIPE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, FireTweaks.id("crafting_shaped"), new FixitShapedRecipe.Serializer());
    }
}
