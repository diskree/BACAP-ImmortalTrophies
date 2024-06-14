package com.diskree.immortaltrophies.mixin;

import com.diskree.immortaltrophies.ImmortalTrophies;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract boolean isRemoved();

    @Shadow
    public abstract void discard();

    @Shadow
    public abstract @Nullable ItemEntity dropStack(ItemStack stack, float yOffset);

    @Inject(
        method = "isInvulnerableTo",
        at = @At("HEAD"),
        cancellable = true
    )
    private void cancelDamage(
        DamageSource damageSource,
        CallbackInfoReturnable<Boolean> cir
    ) {
        Entity entity = (Entity) (Object) this;
        if (isRemoved() || ImmortalTrophies.isTrophy(entity)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(
        method = "tickInVoid",
        at = @At("HEAD"),
        cancellable = true
    )
    private void teleportTrophyFromVoid(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof PersistentProjectileEntity persistentProjectileEntity &&
            ImmortalTrophies.isTrophy(persistentProjectileEntity.getItemStack())
        ) {
            dropStack(persistentProjectileEntity.asItemStack(), 0.1f);
            discard();
            ci.cancel();
        }
        if (ImmortalTrophies.isTrophy(entity)) {
            ImmortalTrophies.teleportTrophyToSafeZone(entity);
            ci.cancel();
        }
    }
}
