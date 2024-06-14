package com.diskree.immortaltrophies.mixin;

import com.diskree.immortaltrophies.ImmortalTrophies;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeMatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeMatcher.class)
public class RecipeMatcherMixin {

    @Inject(
        method = "addInput(Lnet/minecraft/item/ItemStack;I)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void cancelDamage(
        ItemStack stack,
        int maxCount,
        CallbackInfo ci
    ) {
        if (ImmortalTrophies.isTrophy(stack)) {
            ci.cancel();
        }
    }
}
