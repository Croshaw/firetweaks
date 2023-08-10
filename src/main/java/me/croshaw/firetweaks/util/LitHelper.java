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

    public static boolean canBeLit(BlockState state) {
        if(!state.contains(FixitTorchBlock.BURNABLESTATE)) return false;
        return switch (state.get(FixitTorchBlock.BURNABLESTATE)) {
            case UNLIT -> true;
            case SMOLDERING -> true;
            default -> false;
        };
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
            } else if(canBeLit(state))
                world.setBlockState(newPos, state.with(FixitTorchBlock.BURNABLESTATE, FireTweaksProp.LIT), 11);
        }
    }


}
