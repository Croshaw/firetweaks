package me.croshaw.firetweaks.mixin;

import me.croshaw.firetweaks.util.LitHelper;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity {
    @Shadow protected boolean inGround;
    private BlockPos firePos;
    public PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if(!this.world.isClient() && this.inGround && this.isOnFire()) {
            if(this.random.nextInt(200) == 0) {
                if(firePos != null) {
                    if(((FireBlock)Blocks.FIRE).canPlaceAt(Blocks.FIRE.getDefaultState(), world, firePos.up()))
                        this.world.setBlockState(firePos.up(), Blocks.FIRE.getDefaultState());
                    if(this.random.nextFloat() < .5f)
                        LitHelper.LitBlocksAround((ServerWorld) this.world, firePos.up());
                    else
                        LitHelper.LitBlocksAround((ServerWorld) this.world, firePos);
                }
            }
        }
    }

    @Inject(method = "onBlockHit", at = @At("TAIL"))
    private void onBlockHit(BlockHitResult blockHitResult, CallbackInfo ci) {
        firePos = blockHitResult.getBlockPos();
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        if(firePos == null) return;
        nbt.putInt("firePosX", firePos.getX());
        nbt.putInt("firePosY", firePos.getY());
        nbt.putInt("firePosZ", firePos.getZ());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if(nbt.contains("firePosX"))
            firePos = new BlockPos(nbt.getInt("firePosX"), nbt.getInt("firePosY"), nbt.getInt("firePosZ"));
    }


//    private boolean canPlace(BlockPos pos) {
//        return world.getBlockState(pos).getBlock() == Blocks.AIR && world.getBlockState(pos.down()).getBlock() != Blocks.AIR;
//    }
//
//    private BlockPos getFirePos() {
//        if(canPlace(this.getBlockPos()))
//            return this.getBlockPos();
//        if(canPlace(this.getBlockPos().up()))
//            return this.getBlockPos().up();
//        for (var dir : Direction.values()) {
//            if(dir == Direction.DOWN || dir == Direction.UP)
//                continue;
//            if(canPlace(this.getBlockPos().up().offset(dir)))
//                return this.getBlockPos().up().offset(dir);
//            if(canPlace(this.getBlockPos().offset(dir)))
//                return this.getBlockPos().offset(dir);
//        }
//        return null;
//    }
}
