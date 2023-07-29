package me.croshaw.firetweaks.handlers;

import me.croshaw.firetweaks.mixin.FireBlockAccessor;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;

public class ServerTickHandler {
    public static void handle(ServerWorld world) {
        if (world.getTime() % 20 == 0 && world.getGameRules().getBoolean(GameRules.DO_FIRE_TICK)) {
            world.iterateEntities().forEach(entity -> {
                if (entity.isOnFire()) {
                    BlockPos pos = entity.getBlockPos().add(MathHelper.nextInt(world.random, -1, 1), MathHelper.nextInt(world.random, -1, 1), MathHelper.nextInt(world.random, -1, 1));
                    if (world.getBlockState(pos).isAir() && ((FireBlockAccessor) Blocks.FIRE).firetweaks$areBlocksAroundFlammable(world, pos))
                        world.setBlockState(pos, AbstractFireBlock.getState(world, pos));
                }
            });
        }
    }
}
