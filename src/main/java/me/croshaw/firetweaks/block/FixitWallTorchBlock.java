package me.croshaw.firetweaks.block;

import me.croshaw.firetweaks.util.FireTweaksProp;
import me.croshaw.firetweaks.util.StacksUtil;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class FixitWallTorchBlock extends FixitTorchBlock {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;;
    public FixitWallTorchBlock(float fireDamage) {
        super(fireDamage);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return Blocks.WALL_TORCH.canPlaceAt(state, world, pos);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Blocks.WALL_TORCH.getOutlineShape(state, world, pos, context);
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState parentState = Blocks.WALL_TORCH.getPlacementState(ctx);
        if(parentState != null) {
            BlockState state = this.getDefaultState();
            return state.with(FACING, parentState.get(FACING)).with(BURNABLESTATE, StacksUtil.getBlockStateFromStack(ctx.getStack()));
        }
        return null;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return Blocks.WALL_TORCH.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        boolean lit = state.get(BURNABLESTATE) == FireTweaksProp.LIT;
        boolean smoldering = state.get(BURNABLESTATE) == FireTweaksProp.SMOLDERING;
        if(lit || smoldering) {
            Direction direction = state.get(FACING);
            double d = (double) pos.getX() + 0.5;
            double e = (double) pos.getY() + 0.7;
            double f = (double) pos.getZ() + 0.5;
            Direction direction2 = direction.getOpposite();
            world.addParticle(ParticleTypes.SMOKE, d + 0.27 * (double) direction2.getOffsetX(), e + 0.22, f + 0.27 * (double) direction2.getOffsetZ(), 0.0, 0.0, 0.0);
            if(!smoldering)
                world.addParticle(this.particle, d + 0.27 * (double) direction2.getOffsetX(), e + 0.22, f + 0.27 * (double) direction2.getOffsetZ(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return Blocks.WALL_TORCH.rotate(state, rotation);
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return Blocks.WALL_TORCH.mirror(state, mirror);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

}
