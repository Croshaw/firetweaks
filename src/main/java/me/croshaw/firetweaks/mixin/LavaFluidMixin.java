package me.croshaw.firetweaks.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LavaFluid.class)
public abstract class LavaFluidMixin {
    @Shadow protected abstract void playExtinguishEvent(WorldAccess world, BlockPos pos);

    @Inject(method = "onRandomTick", at = @At("HEAD"), cancellable = true)
    private void onRandomTick(World world, BlockPos pos, FluidState state, Random random, CallbackInfo ci) {
        if(!world.isClient() && world.hasRain(pos.up())) {
            world.setBlockState(pos, Blocks.STONE.getDefaultState());
            this.playExtinguishEvent(world, pos);
            ci.cancel();
        }
    }
}
