package me.croshaw.firetweaks.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
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
            if(this.random.nextInt(100) == 0) {
                if(firePos == null)
                    firePos = getFirePos();
                if(firePos != null)
                    this.world.setBlockState(firePos, Blocks.FIRE.getDefaultState());
            }
        }
    }

    private boolean canPlace(BlockPos pos) {
        return world.getBlockState(pos).getBlock() == Blocks.AIR && world.getBlockState(pos.down()).getBlock() != Blocks.AIR;
    }

    private BlockPos getFirePos() {
        if(canPlace(this.getBlockPos()))
            return this.getBlockPos();
        if(canPlace(this.getBlockPos().up()))
            return this.getBlockPos().up();
        for (var dir : Direction.values()) {
            if(dir == Direction.DOWN || dir == Direction.UP)
                continue;
            if(canPlace(this.getBlockPos().up().offset(dir)))
                return this.getBlockPos().up().offset(dir);
            if(canPlace(this.getBlockPos().offset(dir)))
                return this.getBlockPos().offset(dir);
        }
        return null;
    }
}
