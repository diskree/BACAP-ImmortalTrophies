package com.diskree.immortaltrophies.mixin;

import com.diskree.immortaltrophies.ImmortalTrophies;
import net.minecraft.item.ItemStack;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TradeOfferList.class)
public class TradeOfferListMixin {

    @Inject(
        method = "getValidOffer",
        at = @At(value = "HEAD"),
        cancellable = true
    )
    private void cancelTrophyOffer(
        ItemStack firstBuyItem,
        ItemStack secondBuyItem,
        int index,
        CallbackInfoReturnable<TradeOffer> cir
    ) {
        if (ImmortalTrophies.isTrophy(firstBuyItem) || ImmortalTrophies.isTrophy(secondBuyItem)) {
            cir.setReturnValue(null);
        }
    }
}
