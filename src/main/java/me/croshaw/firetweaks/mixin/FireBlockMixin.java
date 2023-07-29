package me.croshaw.firetweaks.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireBlock.class)
public class FireBlockMixin {

    protected boolean isLit(BlockState state) {
        return state.contains(Properties.LIT) && !state.get(Properties.LIT);
    }

    private void LitBlocksAround(ServerWorld world, BlockPos pos) {
        Direction[] directions = Direction.values();
        int size = directions.length;

        for(int i = 0; i < size; ++i) {
            Direction direction = directions[i];
            BlockPos newPos = pos.offset(direction);
            BlockState state = world.getBlockState(newPos);
            if (this.isLit(state)) {
                world.setBlockState(newPos, state.with(Properties.LIT, true), 11);
            }
        }

    }

    @Inject(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/FireBlock;areBlocksAroundFlammable(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        LitBlocksAround(world, pos);
    }
}
