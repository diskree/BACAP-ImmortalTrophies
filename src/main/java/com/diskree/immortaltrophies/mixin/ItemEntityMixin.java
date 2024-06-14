package com.diskree.immortaltrophies.mixin;

import com.diskree.immortaltrophies.ImmortalTrophies;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

    @Unique
    private boolean shouldDespawnImmediately;

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
        method = "setStack",
        at = @At("HEAD")
    )
    private void markAsInvulnerable(ItemStack stack, CallbackInfo ci) {
        if (ImmortalTrophies.isTrophy(stack)) {
            setInvulnerable(true);
        }
    }

    @Inject(
        method = "setDespawnImmediately",
        at = @At("HEAD")
    )
    private void markAsShouldDespawnImmediately(CallbackInfo ci) {
        shouldDespawnImmediately = true;
    }

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/ItemEntity;discard()V",
            ordinal = 1
        )
    )
    private void cancelDespawn(ItemEntity itemEntity) {
        if (shouldDespawnImmediately || !ImmortalTrophies.isTrophy(itemEntity)) {
            itemEntity.discard();
            shouldDespawnImmediately = false;
        }
    }
}
