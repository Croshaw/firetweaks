package me.croshaw.firetweaks.entity.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class FuelBlockEntity extends BlockEntity {
    protected long fuel;
    public FuelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putLong("Fuel", fuel);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        fuel = nbt.contains("Fuel") ? nbt.getLong("Fuel") : 0;
    }

    public long getFuel() {
        return fuel;
    }

    public void setFuel(long newValue) {
        fuel = newValue;
    }
    public void incrementFuel(int increment){
        fuel+=increment;
        if(fuel < 0) {
            fuel = 0;
        }
    }
    public void decrementFuel(int decrement) {
        incrementFuel(-1*decrement);
    }
}
