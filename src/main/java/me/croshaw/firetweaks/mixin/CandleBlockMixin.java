package me.croshaw.firetweaks.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CandleBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.tag.BlockTags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.block.CandleBlock.WATERLOGGED;

@Mixin(CandleBlock.class)
public abstract class CandleBlockMixin {
    @Shadow @Final public static BooleanProperty LIT;

    @Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
    private void getPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
        if(ctx.getPlayer().isOnFire() && canBeLit(cir.getReturnValue()))
            cir.setReturnValue(cir.getReturnValue().with(LIT, true));
    }

    public boolean canBeLit(BlockState state) {
        return state.isIn(BlockTags.CANDLES, (statex) -> statex.contains(LIT) && statex.contains(WATERLOGGED)) && !(Boolean)state.get(LIT) && !(Boolean)state.get(WATERLOGGED);
    }
}
