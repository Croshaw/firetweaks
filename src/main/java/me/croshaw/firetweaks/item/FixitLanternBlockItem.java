package me.croshaw.firetweaks.item;

import me.croshaw.firetweaks.FireTweaks;
import me.croshaw.firetweaks.config.FireTweaksConfig;
import me.croshaw.firetweaks.util.StacksUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class FixitLanternBlockItem extends BlockItem {
    public final int maxFuel;
    public FixitLanternBlockItem(Block block, int maxFuel) {
        super(block, new Settings().maxCount(1));
        this.maxFuel = maxFuel;
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if(clickType == ClickType.RIGHT) {
            if(!FireTweaksConfig.getFuelItemsBlackList().contains(StacksUtil.getKey(otherStack.getItem())) && FireTweaksConfig.getFuelItems().containsKey(otherStack.getItem())) {
                long curFuel = StacksUtil.getFuel(stack);
                if(curFuel < maxFuel) {
                    int tempFuel = FireTweaksConfig.getFuelItems().get(otherStack.getItem());
                    int fuel = maxFuel <= tempFuel+curFuel ? maxFuel-(int)curFuel : tempFuel;
                    StacksUtil.consumeStack(otherStack, player, null);
                    StacksUtil.addFuel(stack, fuel);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if(user.isSneaky() && StacksUtil.getFuel(stack) > 0) {
            stack.getOrCreateNbt().putBoolean(FireTweaks.NBT_LIT_KEY, !StacksUtil.isLit(stack));
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return StacksUtil.getFuel(stack) < maxFuel;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return maxFuel != 0 ? Math.round(13.0f - (maxFuel - StacksUtil.getFuel(stack)) * 13.0f / maxFuel) : 0;
    }
    @Override
    public int getItemBarColor(ItemStack stack) {
        return MathHelper.hsvToRgb(3.0f, 1.0f, 1.0f);
    }
}
