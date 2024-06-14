package com.diskree.immortaltrophies.mixin;

import com.diskree.immortaltrophies.ImmortalTrophies;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {

    @Redirect(
        method = "updateResult",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isEmpty()Z",
            ordinal = 0
        )
    )
    private boolean cancelResultForTrophy(@NotNull ItemStack stack) {
        return stack.isEmpty() || ImmortalTrophies.isTrophy(stack);
    }
}