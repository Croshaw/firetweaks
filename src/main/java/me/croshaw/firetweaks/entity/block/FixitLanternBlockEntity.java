package me.croshaw.firetweaks.entity.block;

import me.croshaw.firetweaks.block.FixitLanternBlock;
import me.croshaw.firetweaks.registry.BlocksRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FixitLanternBlockEntity extends FuelBlockEntity {
    public FixitLanternBlockEntity(BlockPos pos, BlockState state) {
        super(BlocksRegistry.FIXIT_LANTERN_BLOCK_ENTITY, pos, state);
        fuel = 0;
    }

    public static void litServerTick(World world, BlockPos pos, BlockState state, FixitLanternBlockEntity fixitTorch) {
        fixitTorch.decrementFuel(1);
        if(fixitTorch.fuel == 0)
            world.setBlockState(pos, state.with(FixitLanternBlock.UNFIREABLE_LIT, false), 11);
    }
}
