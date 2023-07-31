package me.croshaw.firetweaks.entity.block;

import me.croshaw.firetweaks.block.FixitTorchBlock;
import me.croshaw.firetweaks.config.FireTweaksConfig;
import me.croshaw.firetweaks.registry.BlocksRegistry;
import me.croshaw.firetweaks.util.FireTweaksProp;
import net.minecraft.block.BlockState;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FixitTorchBlockEntity extends FuelBlockEntity {
    public FixitTorchBlockEntity(BlockPos pos, BlockState state) {
        super(BlocksRegistry.FIXIT_TORCH_BLOCK_ENTITY, pos, state);
        fuel = FireTweaksConfig.getLitTorchFuel()*20;
    }

    public static void litServerTick(World world, BlockPos pos, BlockState state, FixitTorchBlockEntity fixitTorch) {
        fixitTorch.decrementFuel(1);
        if(fixitTorch.fuel == 0) {
            world.setBlockState(pos, state.with(FixitTorchBlock.BURNABLESTATE, FireTweaksProp.SMOLDERING), 11);
            fixitTorch.setFuel(FireTweaksConfig.getSmolderingTorchFuel()*20);
        }
        if(world.hasRain(pos) && world.getRandom().nextFloat()*100 < FireTweaksConfig.getChanceExtinguishByRain()) {
            world.setBlockState(pos, state.with(FixitTorchBlock.BURNABLESTATE, FireTweaksProp.UNLIT), 11);
            world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    public static void smolderingServerTick(World world, BlockPos pos, BlockState state, FixitTorchBlockEntity fixitTorch) {
        fixitTorch.decrementFuel(1);
        if(fixitTorch.fuel == 0) {
            world.setBlockState(pos, state.with(FixitTorchBlock.BURNABLESTATE, FireTweaksProp.BURNT), 11);
            world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }
}
