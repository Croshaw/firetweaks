package me.croshaw.firetweaks.mixin;

import me.croshaw.firetweaks.event.AttackEntityCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setHealth(F)V"))
    private void applyDamage(DamageSource source, float amount, CallbackInfo ci) {
        if(source.getSource() != null && source.getSource() instanceof LivingEntity livingEntity) {
            LivingEntity thys = (LivingEntity) (Object)this;
            AttackEntityCallback.EVENT.invoker().interact(livingEntity, thys.getWorld(), livingEntity.getActiveHand(), thys, null);
        }
    }
}
