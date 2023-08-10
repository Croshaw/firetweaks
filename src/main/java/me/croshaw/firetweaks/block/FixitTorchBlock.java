package me.croshaw.firetweaks.block;

import me.croshaw.firetweaks.config.FireTweaksConfig;
import me.croshaw.firetweaks.entity.block.FixitTorchBlockEntity;
import me.croshaw.firetweaks.entity.block.FuelBlockEntity;
import me.croshaw.firetweaks.registry.BlocksRegistry;
import me.croshaw.firetweaks.util.FireTweaksProp;
import me.croshaw.firetweaks.util.LitHelper;
import me.croshaw.firetweaks.util.StacksUtil;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class FixitTorchBlock extends BlockWithEntity {
    public static final EnumProperty<FireTweaksProp> BURNABLESTATE = EnumProperty.of("burnablestate", FireTweaksProp.class);;
    private final float fireDamage;
    protected static final VoxelShape BOUNDING_SHAPE = Block.createCuboidShape(7, 0, 7, 9, 10, 9);
    protected final ParticleEffect particle;
    public FixitTorchBlock(float fireDamage) {
        super(Settings.of(Material.DECORATION).breakInstantly().luminance((state) -> state.get(BURNABLESTATE) == FireTweaksProp.LIT ? 14 : state.get(BURNABLESTATE) == FireTweaksProp.SMOLDERING ? 5 : 0).sounds(BlockSoundGroup.WOOD));
        particle = ParticleTypes.FLAME;
        this.fireDamage = fireDamage;
        this.setDefaultState(this.getStateManager().getDefaultState().with(BURNABLESTATE, FireTweaksProp.UNLIT));
    }
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return BOUNDING_SHAPE;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return Blocks.TORCH.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return Blocks.TORCH.canPlaceAt(state, world, pos);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        boolean lit = state.get(BURNABLESTATE) == FireTweaksProp.LIT;
        boolean smoldering = state.get(BURNABLESTATE) == FireTweaksProp.SMOLDERING;
        if(lit || smoldering) {
            double d = (double) pos.getX() + 0.5;
            double e = (double) pos.getY() + 0.7;
            double f = (double) pos.getZ() + 0.5;
            world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
            if(!smoldering)
                world.addParticle(this.particle, d, e, f, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (state.get(BURNABLESTATE) == FireTweaksProp.LIT && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity)) {
            entity.damage(DamageSource.IN_FIRE, this.fireDamage);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if(state.get(BURNABLESTATE) == FireTweaksProp.LIT && stack.isEmpty()) {
            world.setBlockState(pos, state.with(BURNABLESTATE, FireTweaksProp.UNLIT));
            return ActionResult.success(true);
        } else if(LitHelper.canBeLit(state)) {
            if(stack.isOf(Items.FLINT_AND_STEEL)) {
                stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
                world.setBlockState(pos, state.with(BURNABLESTATE, FireTweaksProp.LIT));
                if(world.getBlockEntity(pos) instanceof FuelBlockEntity fuelBlockEntity)
                    fuelBlockEntity.setFuel(FireTweaksConfig.getLitTorchFuel()*20);
                return ActionResult.success(true);
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if(world.isClient)
            return null;
        return switch (state.get(BURNABLESTATE)) {
            case LIT -> checkType(type, BlocksRegistry.FIXIT_TORCH_BLOCK_ENTITY, FixitTorchBlockEntity::litServerTick);
            case SMOLDERING ->
                    checkType(type, BlocksRegistry.FIXIT_TORCH_BLOCK_ENTITY, FixitTorchBlockEntity::smolderingServerTick);
            default -> null;
        };
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(BURNABLESTATE, StacksUtil.getBlockStateFromStack(ctx.getStack()));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FixitTorchBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BURNABLESTATE);
    }
}
