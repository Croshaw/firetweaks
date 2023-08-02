package me.croshaw.firetweaks.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Ingredient.class)
public class IngredientMixin {
    ItemStack stack;

    @Inject(method = "test(Lnet/minecraft/item/ItemStack;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
    private void getItem(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        stack = itemStack;
    }

    @Redirect(method = "test(Lnet/minecraft/item/ItemStack;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean test(ItemStack instance, Item item) {
        if(instance.hasNbt() || stack.hasNbt())
            return ItemStack.areNbtEqual(instance, stack);
        return instance.isItemEqual(stack);
    }
}
