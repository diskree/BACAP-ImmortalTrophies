package com.diskree.immortaltrophies.mixin;

import com.diskree.immortaltrophies.ImmortalTrophies;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @ModifyVariable(
        method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
        at = @At("HEAD"),
        index = 1,
        argsOnly = true
    )
    private ItemStack overrideItemForRender(ItemStack stack) {
        if (ImmortalTrophies.isTrophy(stack)) {
            return ImmortalTrophies.getRealItem(stack);
        }
        return stack;
    }

    @ModifyVariable(
        method = "getModel",
        at = @At("HEAD"),
        index = 1,
        argsOnly = true
    )
    private ItemStack overrideItemModel(ItemStack stack) {
        if (ImmortalTrophies.isTrophy(stack)) {
            return ImmortalTrophies.getRealItem(stack);
        }
        return stack;
    }
}
