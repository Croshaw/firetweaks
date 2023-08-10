package me.croshaw.firetweaks.handlers;

import me.croshaw.firetweaks.config.FireTweaksConfig;
import me.croshaw.firetweaks.registry.ItemsRegistry;
import me.croshaw.firetweaks.util.FireTweaksProp;
import me.croshaw.firetweaks.util.StacksUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class AttackEntityHandler {
    enum ItemType {
        TORCH {
            @Override
            public boolean isConsume() {
                return FireTweaksConfig.getConsumeTorch();
            }
        }, CANDLE {
            @Override
            public boolean isConsume() {
                return FireTweaksConfig.getConsumeCandle();
            }
        }, FLAMMABLE {
            @Override
            public boolean isConsume() {
                return FireTweaksConfig.getConsumeFlammableItems();
            }
        }, SOUL_TORCH {
            @Override
            public boolean isConsume() {
                return FireTweaksConfig.getConsumeTorch();
            }
        }, NONE {
            @Override
            public boolean isConsume() {
                return false;
            }
        };
        public abstract boolean isConsume();
    }
    public static ActionResult handle(LivingEntity attacker, World world, Hand hand, Entity target, EntityHitResult hitResult) {
        if(!attacker.getWorld().isClient() && !attacker.isSpectator()) {
            if(!target.isFireImmune()) {
                ItemStack stack = attacker.getStackInHand(hand);
                ItemType type = getItemType(stack);
                if(type != ItemType.NONE)
                    attack(attacker, target, stack, type);
                else if(FireTweaksConfig.getAllowHitByBurnEntity() && attacker.isOnFire())
                    burn(target, FireTweaksConfig.getHitDuration(), ItemType.NONE, FireTweaksConfig.getFireChanceWhenBurn());
            }
        }
        return ActionResult.PASS;
    }
    private static void attack(LivingEntity attacker, Entity target, ItemStack stack, ItemType type) {
        consumeItem(attacker, stack, type, burn(target, FireTweaksConfig.getHitDuration(), type, FireTweaksConfig.getFireChanceByAllowItems()));
    }

    private static void consumeItem(LivingEntity attacker, ItemStack stack, ItemType type, int fireSeconds) {
        if ((attacker instanceof PlayerEntity player && !player.isCreative()) && (type.isConsume() || stack.isDamageable()) && (FireTweaksConfig.getConsumeWithoutFire() || fireSeconds > 0)) {
            if(stack.isDamageable())
                stack.damage(1, attacker, player1 -> {});
            else
                stack.decrement(1);
        }
    }
    private static int burn(Entity target, int defaultDuration, ItemType type, int chance) {
        int fireSeconds = getFireSeconds(target, defaultDuration, type, chance);
        if (fireSeconds > 0)
            target.setOnFireFor(fireSeconds);
        return fireSeconds;
    }
    private static int getFireSeconds(Entity target, int fireDuration, ItemType type, int chance) {
        if ((Math.random() * 100) < chance) {
            if (type == ItemType.SOUL_TORCH) {
                if (target instanceof AbstractPiglinEntity)
                    return fireDuration * 2;
                return fireDuration + 1;
            }
            return fireDuration;
        }
        return 0;
    }
    private static ItemType getItemType(ItemStack stack) {
        if(isTorch(stack))
            return ItemType.TORCH;
        if(isCandle(stack))
            return ItemType.CANDLE;
        if(isFlammable(stack))
            return ItemType.FLAMMABLE;
        if(isSoulTorch(stack))
            return ItemType.SOUL_TORCH;
        return ItemType.NONE;
    }
    private static boolean isTorch(ItemStack stack) {
        return ((stack.isOf(Items.TORCH) || (stack.isOf(ItemsRegistry.TORCH_ITEM) && StacksUtil.getBlockStateFromStack(stack) == FireTweaksProp.LIT)) && FireTweaksConfig.getVanillaTorchesEnabled()) || FireTweaksConfig.getExtraTorchItems().contains(StacksUtil.getKey(stack.getItem())) || isSoulTorch(stack);
    }

    private static boolean isCandle(ItemStack stack) {
        return FireTweaksConfig.getAllowCandles() && stack.isIn(ItemTags.CANDLES);
    }

    private static boolean isFlammable(ItemStack stack) {
        return FireTweaksConfig.getAllowFlammableItems() && FireTweaksConfig.getExtraFlammableItems().contains(StacksUtil.getKey(stack.getItem()));
    }

    private static boolean isSoulTorch(ItemStack stack) {
        return (stack.isOf(Items.SOUL_TORCH) && FireTweaksConfig.getVanillaTorchesEnabled()) || FireTweaksConfig.getExtraSoulTorchItems().contains(StacksUtil.getKey(stack.getItem()));
    }
}
