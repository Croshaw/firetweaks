package me.croshaw.firetweaks.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "isFireImmune", at = @At("RETURN"), cancellable = true)
    private void isFireImmune(CallbackInfoReturnable<Boolean> cir) {
        Entity thys = (Entity)(Object)this;
        if(thys instanceof LivingEntity livingEntity) {
            if(livingEntity.getActiveStatusEffects().containsKey(StatusEffects.FIRE_RESISTANCE))
                cir.setReturnValue(true);
        }
    }
}
