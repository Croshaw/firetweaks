package me.croshaw.firetweaks.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public interface AttackEntityCallback {
    Event<AttackEntityCallback> EVENT = EventFactory.createArrayBacked(AttackEntityCallback.class,
            (listeners) -> (player, world, hand, entity, hitResult) -> {
                for (AttackEntityCallback event : listeners) {
                    ActionResult result = event.interact(player, world, hand, entity, hitResult);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            }
    );

    ActionResult interact(LivingEntity attacker, World world, Hand hand, LivingEntity target, @Nullable EntityHitResult hitResult);
}
