package me.croshaw.firetweaks.block;

import me.croshaw.firetweaks.config.FireTweaksConfig;
import me.croshaw.firetweaks.entity.block.FixitLanternBlockEntity;
import me.croshaw.firetweaks.entity.block.FuelBlockEntity;
import me.croshaw.firetweaks.registry.BlocksRegistry;
import me.croshaw.firetweaks.util.StacksUtil;
import net.fabricmc.fabric.impl.content.registry.FuelRegistryImpl;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.FurnaceFuelSlot;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class FixitLanternBlock extends BlockWithEntity implements Waterloggable {
    public static final BooleanProperty UNFIREABLE_LIT = BooleanProperty.of("unfireable_lit");
    public FixitLanternBlock() {
        super(AbstractBlock.Settings.of(Material.METAL).requiresTool().strength(3.5F).sounds(BlockSoundGroup.LANTERN).luminance((state) -> state.get(UNFIREABLE_LIT) ? 15 : 0).nonOpaque());
        this.setDefaultState(this.stateManager.getDefaultState().with(Properties.HANGING, false).with(Properties.WATERLOGGED, false).with(UNFIREABLE_LIT, false));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if(!FireTweaksConfig.getFuelItemsBlackList().contains(StacksUtil.getKey(stack.getItem())) && FireTweaksConfig.getFuelItems().containsKey(stack.getItem()) && world.getBlockEntity(pos) instanceof FuelBlockEntity fuelBlockEntity) {
            if(fuelBlockEntity.getFuel() < BlocksRegistry.LANTERN_ITEM.maxFuel) {
                int tempFuel = FireTweaksConfig.getFuelItems().get(stack.getItem());
                int fuel = BlocksRegistry.LANTERN_ITEM.maxFuel <= tempFuel+fuelBlockEntity.getFuel() ? BlocksRegistry.LANTERN_ITEM.maxFuel-(int)fuelBlockEntity.getFuel() : tempFuel;
                StacksUtil.consumeStack(stack, player, hand);
                fuelBlockEntity.incrementFuel(fuel);
                return ActionResult.success(true);
            }
        } else if(player.isSneaky() && world.getBlockEntity(pos) instanceof FuelBlockEntity fuelBlockEntity && fuelBlockEntity.getFuel() > 0) {
            world.setBlockState(pos, state.with(UNFIREABLE_LIT, !state.get(UNFIREABLE_LIT)));
            return ActionResult.success(true);
        }
        return ActionResult.PASS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState parentState = Blocks.LANTERN.getPlacementState(ctx);
        if(parentState != null)
            return this.getDefaultState().with(Properties.HANGING, parentState.get(Properties.HANGING)).with(Properties.WATERLOGGED, parentState.get(Properties.WATERLOGGED)).with(UNFIREABLE_LIT, StacksUtil.isLit(ctx.getStack()));
        return null;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Blocks.LANTERN.getOutlineShape(state, world, pos, context);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return Blocks.LANTERN.canPlaceAt(state, world, pos);
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return Blocks.LANTERN.getPistonBehavior(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return Blocks.LANTERN.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return Blocks.LANTERN.getFluidState(state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(blockEntity instanceof FuelBlockEntity fuelBlockEntity && itemStack.isOf(BlocksRegistry.LANTERN_ITEM))
            fuelBlockEntity.setFuel(StacksUtil.getFuel(itemStack));
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FixitLanternBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if(world.isClient || !state.get(UNFIREABLE_LIT))
            return null;
        return checkType(type, BlocksRegistry.FIXIT_LANTERN_BLOCK_ENTITY, FixitLanternBlockEntity::litServerTick);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(UNFIREABLE_LIT, Properties.WATERLOGGED, Properties.HANGING);
    }
}
