package me.croshaw.firetweaks.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import me.croshaw.firetweaks.block.FixitTorchBlock;
import me.croshaw.firetweaks.registry.LootRegistry;
import me.croshaw.firetweaks.util.FireTweaksProp;
import me.croshaw.firetweaks.util.StacksUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;

public class TorchLootFunction extends ConditionalLootFunction {
    protected TorchLootFunction(LootCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        if(context.get(LootContextParameters.BLOCK_STATE).getBlock().asItem().equals(Items.TORCH) || context.get(LootContextParameters.BLOCK_STATE).get(FixitTorchBlock.BURNABLESTATE) == FireTweaksProp.UNLIT)
            return StacksUtil.createStack(FireTweaksProp.UNLIT);
        return new ItemStack(Items.STICK, 1);
    }

    @Override
    public LootFunctionType getType() {
        return LootRegistry.FIXIT_TORCH_LOOT_FUNCTION;
    }

    public static class Serializer extends ConditionalLootFunction.Serializer<TorchLootFunction> {
        @Override
        public TorchLootFunction fromJson(JsonObject json, JsonDeserializationContext context, LootCondition[] conditions) {
            return new TorchLootFunction(conditions);
        }
    }
}
