package com.diskree.immortaltrophies.mixin;

import com.diskree.immortaltrophies.ImmortalTrophies;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin {

    @Shadow
    private ItemStack stack;

    @Inject(
        method = "age",
        at = @At("HEAD"),
        cancellable = true
    )
    private void cancelDespawn(CallbackInfo ci) {
        if (ImmortalTrophies.isTrophy(stack)) {
            ci.cancel();
        }
    }
}
