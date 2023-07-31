package me.croshaw.firetweaks.registry;

import me.croshaw.firetweaks.FireTweaks;
import me.croshaw.firetweaks.block.FixitTorchBlock;
import me.croshaw.firetweaks.block.FixitWallTorchBlock;
import me.croshaw.firetweaks.entity.block.FixitTorchBlockEntity;
import me.croshaw.firetweaks.item.FixitWallStandingBlockItem;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class BlocksRegistry {
    public final static FixitTorchBlock TORCH_BLOCK = new FixitTorchBlock(1f);
    public final static FixitWallTorchBlock TORCH_WALL_BLOCK = new FixitWallTorchBlock(1f);
    public final static FixitWallStandingBlockItem TORCH_ITEM = new FixitWallStandingBlockItem(TORCH_BLOCK, TORCH_WALL_BLOCK, new Item.Settings());

    public static BlockEntityType<FixitTorchBlockEntity> FIXIT_TORCH_BLOCK_ENTITY;


    public static void registry() {
        FIXIT_TORCH_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, FireTweaks.id("torch_block_entity"), FabricBlockEntityTypeBuilder.create(FixitTorchBlockEntity::new, TORCH_BLOCK, TORCH_WALL_BLOCK).build(null));
        Registry.register(Registry.BLOCK, FireTweaks.id("torch"), TORCH_BLOCK);
        Registry.register(Registry.BLOCK, FireTweaks.id("wall_torch"), TORCH_WALL_BLOCK);
        Registry.register(Registry.ITEM, FireTweaks.id("torch"), TORCH_ITEM);
    }
}
