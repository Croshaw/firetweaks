package me.croshaw.firetweaks.item;

import me.croshaw.firetweaks.config.FireTweaksConfig;
import me.croshaw.firetweaks.registry.ItemsRegistry;
import me.croshaw.firetweaks.util.FireTweaksProp;
import me.croshaw.firetweaks.util.LitHelper;
import me.croshaw.firetweaks.util.StacksUtil;
import net.minecraft.block.Block;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class FixitWallStandingBlockItem extends WallStandingBlockItem {
    public FixitWallStandingBlockItem(Block standingBlock, Block wallBlock) {
        super(standingBlock, wallBlock, new Settings());
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if(clickType == ClickType.RIGHT) {
            if(LitHelper.canBeLit(StacksUtil.getBlockStateFromStack(stack))) {
                if (FireTweaksConfig.getLightItems().contains(StacksUtil.getKey(otherStack.getItem())) || otherStack.isOf(ItemsRegistry.FIRE_STARTER_ITEM)) {
                    StacksUtil.modifyStack(stack, FireTweaksProp.LIT);
                    StacksUtil.consumeStack(otherStack, player, null, true);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack getDefaultStack() {
        return StacksUtil.modifyStack(super.getDefaultStack(), FireTweaksProp.UNLIT);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if(StacksUtil.getBlockStateFromStack(stack) == FireTweaksProp.BURNT) {
            ItemStack giveStack = new ItemStack(Items.STICK, 4);
            if (!user.giveItemStack(giveStack))
                user.dropStack(giveStack);
            stack.decrement(1);
            TypedActionResult.success(stack,true);
        }
        return TypedActionResult.pass(stack);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(context.getWorld().getBlockState(context.getBlockPos()).getBlock() instanceof FireBlock) {
            if (StacksUtil.getBlockStateFromStack(context.getStack()) == FireTweaksProp.UNLIT) {
                if (!context.getWorld().isClient()) {
                    ItemStack giveStack = StacksUtil.createStack(FireTweaksProp.LIT);
                    if (!context.getPlayer().giveItemStack(giveStack))
                        context.getPlayer().dropStack(giveStack);
                    context.getStack().decrement(1);
                }
                return ActionResult.SUCCESS;
            }
        }
        return super.useOnBlock(context);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(world.isClient()) return;
        if(world.hasRain(entity.getBlockPos())) {
            if(StacksUtil.getOrDefault(stack, 0) == 1 && world.getRandom().nextFloat()*100 < FireTweaksConfig.getChanceExtinguishByRain()) {
                StacksUtil.modifyStack(stack, FireTweaksProp.UNLIT);
                world.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
        else if(entity.isOnFire()) {
            if(world.getRandom().nextFloat() <= .25f && StacksUtil.getOrDefault(stack, 0) == 0)
                StacksUtil.modifyStack(stack, FireTweaksProp.LIT);
        }
    }
}
