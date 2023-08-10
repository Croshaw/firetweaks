package me.croshaw.firetweaks.mixin;

import me.croshaw.firetweaks.config.FireTweaksConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArrowItem.class)
public class ArrowItemMixin {
    @Inject(method = "createArrow", at = @At("RETURN"), cancellable = true)
    private void createArrow(World world, ItemStack stack, LivingEntity shooter, CallbackInfoReturnable<PersistentProjectileEntity> cir) {
        if(shooter.isOnFire() && shooter.getRandom().nextFloat() * 100 < FireTweaksConfig.getArrowFireChance())
            cir.getReturnValue().setOnFireFor(100);
    }
}
