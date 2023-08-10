package me.croshaw.firetweaks.item;

import me.croshaw.firetweaks.block.FixitTorchBlock;
import me.croshaw.firetweaks.config.FireTweaksConfig;
import me.croshaw.firetweaks.util.FireTweaksProp;
import me.croshaw.firetweaks.util.LitHelper;
import me.croshaw.firetweaks.util.StacksUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class FireStarterItem extends Item {
    private final int useDuration;
    public FireStarterItem(int useDuration) {
        super(new Settings().maxDamage(16));
        this.useDuration = useDuration;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if(!world.isClient) {
            BlockHitResult hit = world.raycast(new RaycastContext(user.getEyePos(), user.getEyePos().add(user.getRotationVecClient().multiply(4)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, user));
            BlockPos pos = hit.getBlockPos();
            BlockState state = world.getBlockState(pos);
            if(user.getRandom().nextFloat()*100 < FireTweaksConfig.getFireStarterChanceOfArson()) {
                if (LitHelper.canLit(state)) {
                    world.setBlockState(pos, state.with(Properties.LIT, true));
                    StacksUtil.consumeStack(stack, (PlayerEntity) user, user.getActiveHand());
                } else if (LitHelper.canBeLit(state)) {
                    world.setBlockState(pos, state.with(FixitTorchBlock.BURNABLESTATE, FireTweaksProp.LIT));
                    StacksUtil.consumeStack(stack, (PlayerEntity) user, user.getActiveHand());
                } else if (FireTweaksConfig.getSimulateFlintAndSteel()) {
                    Items.FLINT_AND_STEEL.useOnBlock(new ItemUsageContext((PlayerEntity) user, user.getActiveHand(), hit));
                }
            }
        }
        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return useDuration;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
}
