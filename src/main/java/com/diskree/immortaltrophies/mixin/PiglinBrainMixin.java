package com.diskree.immortaltrophies.mixin;

import com.diskree.immortaltrophies.ImmortalTrophies;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinBrain.class)
public class PiglinBrainMixin {

    @Inject(
        method = "isGoldenItem",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void ignoreTrophy(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (ImmortalTrophies.isTrophy(stack)) {
            cir.setReturnValue(false);
        }
    }
}
