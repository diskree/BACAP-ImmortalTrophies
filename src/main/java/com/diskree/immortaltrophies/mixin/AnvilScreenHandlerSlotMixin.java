package com.diskree.immortaltrophies.mixin;

import com.diskree.immortaltrophies.ImmortalTrophies;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.screen.ForgingScreenHandler$2")
public class AnvilScreenHandlerSlotMixin extends Slot {

    public AnvilScreenHandlerSlotMixin(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Inject(
        method = "canTakeItems",
        at = @At("HEAD"),
        cancellable = true
    )
    private void cancelTakeForTrophy(
        PlayerEntity playerEntity,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (ImmortalTrophies.isTrophy(getStack())) {
            cir.setReturnValue(false);
        }
    }
}
