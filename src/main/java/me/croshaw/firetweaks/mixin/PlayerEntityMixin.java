package me.croshaw.firetweaks.mixin;

import me.croshaw.firetweaks.event.AttackEntityCallback;
import me.croshaw.firetweaks.handlers.AttackEntityHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setHealth(F)V"))
    private void applyDamage(DamageSource source, float amount, CallbackInfo ci) {
        if(source.getAttacker() != null && source.getAttacker() instanceof LivingEntity livingEntity) {
            LivingEntity thys = (LivingEntity) (Object)this;
            AttackEntityCallback.EVENT.invoker().interact(livingEntity, thys.getWorld(), livingEntity.getActiveHand(), thys, null);
        }
    }
}
