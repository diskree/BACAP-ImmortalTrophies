package com.diskree.immortaltrophies.mixin;

import com.diskree.immortaltrophies.ImmortalTrophies;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemPlacementContext.class)
public class ItemPlacementContextMixin extends ItemUsageContext {

    public ItemPlacementContextMixin(PlayerEntity player, Hand hand, BlockHitResult hit) {
        super(player, hand, hit);
    }

    @Inject(
        method = "canPlace",
        at = @At(value = "HEAD"),
        cancellable = true
    )
    private void cancelTrophyPlace(CallbackInfoReturnable<Boolean> cir) {
        if (ImmortalTrophies.isTrophy(getStack())) {
            cir.setReturnValue(false);
        }
    }
}
