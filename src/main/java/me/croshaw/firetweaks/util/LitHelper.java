package me.croshaw.firetweaks.util;

import me.croshaw.firetweaks.block.FixitTorchBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class LitHelper {
    private static boolean canLit(BlockState state) {
        return state.contains(Properties.LIT) && !state.get(Properties.LIT);
    }

    private static boolean canCustomLit(BlockState state) {
        return state.contains(FixitTorchBlock.BURNABLESTATE) && state.get(FixitTorchBlock.BURNABLESTATE) == FireTweaksProp.UNLIT;
    }

    public static void LitBlocksAround(ServerWorld world, BlockPos pos) {
        Direction[] directions = Direction.values();
        int size = directions.length;

        for(int i = 0; i < size; ++i) {
            Direction direction = directions[i];
            BlockPos newPos = pos.offset(direction);
            BlockState state = world.getBlockState(newPos);
            if (canLit(state)) {
                world.setBlockState(newPos, state.with(Properties.LIT, true), 11);
            } else if(canCustomLit(state))
                world.setBlockState(newPos, state.with(FixitTorchBlock.BURNABLESTATE, FireTweaksProp.LIT), 11);
        }
    }
}
