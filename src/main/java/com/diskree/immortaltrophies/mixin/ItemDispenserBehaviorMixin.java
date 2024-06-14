package com.diskree.immortaltrophies.mixin;

import com.diskree.immortaltrophies.ImmortalTrophies;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemDispenserBehavior.class)
public abstract class ItemDispenserBehaviorMixin {

    @Shadow
    protected abstract ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack);

    @Redirect(
        method = "dispense",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/dispenser/ItemDispenserBehavior;dispenseSilently(Lnet/minecraft/util/math/BlockPointer;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack cancelTrophyDispense(
        ItemDispenserBehavior behavior,
        BlockPointer pointer,
        ItemStack stack
    ) {
        if (ImmortalTrophies.isTrophy(stack)) {
            return stack;
        }
        return dispenseSilently(pointer, stack);
    }
}
