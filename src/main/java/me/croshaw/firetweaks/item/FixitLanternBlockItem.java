package me.croshaw.firetweaks.item;

import me.croshaw.firetweaks.FireTweaks;
import me.croshaw.firetweaks.util.StacksUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class FixitLanternBlockItem extends BlockItem {
    private final int maxFuel;
    public FixitLanternBlockItem(Block block, int maxFuel) {
        super(block, new Settings().maxCount(1));
        this.maxFuel = maxFuel;
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
