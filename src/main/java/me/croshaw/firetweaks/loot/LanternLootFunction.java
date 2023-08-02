package me.croshaw.firetweaks.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import me.croshaw.firetweaks.FireTweaks;
import me.croshaw.firetweaks.block.FixitLanternBlock;
import me.croshaw.firetweaks.entity.block.FuelBlockEntity;
import me.croshaw.firetweaks.registry.BlocksRegistry;
import me.croshaw.firetweaks.registry.LootRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;

public class LanternLootFunction extends ConditionalLootFunction {
    protected LanternLootFunction(LootCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        ItemStack stack1 = new ItemStack(BlocksRegistry.LANTERN_ITEM, 1);
        BlockEntity blockEntity = context.get(LootContextParameters.BLOCK_ENTITY);
        if(blockEntity instanceof FuelBlockEntity fuelBlockEntity)
            stack1.getOrCreateNbt().putLong(FireTweaks.NBT_FUEL_KEY, fuelBlockEntity.getFuel());
        stack1.getNbt().putBoolean(FireTweaks.NBT_LIT_KEY, context.get(LootContextParameters.BLOCK_STATE).get(FixitLanternBlock.UNFIREABLE_LIT));
        return stack1;
    }

    @Override
    public LootFunctionType getType() {
        return LootRegistry.FIXIT_LANTERN_LOOT_FUNCTION;
    }

    public static class Serializer extends ConditionalLootFunction.Serializer<LanternLootFunction> {
        @Override
        public LanternLootFunction fromJson(JsonObject json, JsonDeserializationContext context, LootCondition[] conditions) {
            return new LanternLootFunction(conditions);
        }
    }
}
