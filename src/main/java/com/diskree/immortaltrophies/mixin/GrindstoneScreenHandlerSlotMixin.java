package com.diskree.immortaltrophies.mixin;

import com.diskree.immortaltrophies.ImmortalTrophies;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
    targets = {
        "net.minecraft.screen.GrindstoneScreenHandler$2",
        "net.minecraft.screen.GrindstoneScreenHandler$3"
    }
)
public abstract class GrindstoneScreenHandlerSlotMixin extends Slot {

    public GrindstoneScreenHandlerSlotMixin(
        Inventory inventory,
        int index,
        int x,
        int y
    ) {
        super(inventory, index, x, y);
    }

    @Inject(
        method = "canInsert(Lnet/minecraft/item/ItemStack;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void disallowTrophyInsert(
        ItemStack stack,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (ImmortalTrophies.isTrophy(stack)) {
            cir.setReturnValue(false);
        }
    }
}
