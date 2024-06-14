package com.diskree.immortaltrophies.mixin;

import com.diskree.immortaltrophies.ImmortalTrophies;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.component.type.UnbreakableComponent;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.GiveCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(GiveCommand.class)
public class GiveCommandMixin {

    @Unique
    private static boolean isBlackListedItem(Item item) {
        boolean isHead = item instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractSkullBlock || item == Items.CARVED_PUMPKIN;
        return isHead ||
            item == Items.SHIELD ||
            item == Items.TRIDENT ||
            item == Items.FISHING_ROD ||
            item == Items.BOW ||
            item == Items.CROSSBOW ||
            item == Items.GOAT_HORN ||
            item == Items.SPYGLASS ||
            item == Items.ELYTRA ||
            item == Items.WRITTEN_BOOK ||
            item == Items.FILLED_MAP ||
            item.getComponents().contains(DataComponentTypes.JUKEBOX_PLAYABLE) ||
            item instanceof ToolItem ||
            item instanceof ArmorItem;
    }

    @Redirect(
        method = "execute",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/command/argument/ItemStackArgument;createStack(IZ)Lnet/minecraft/item/ItemStack;",
            ordinal = 1
        )
    )
    private static ItemStack overrideTrophy(
        @NotNull ItemStackArgument itemStackArgument,
        int amount,
        boolean checkOverstack,
        @Local ServerPlayerEntity serverPlayerEntity
    ) throws CommandSyntaxException {
        ItemStack stack;
        NbtComponent customData = itemStackArgument.components.get(DataComponentTypes.CUSTOM_DATA).orElse(null);
        if (ImmortalTrophies.isTrophy(customData)) {
            ComponentChanges.Builder editor = ComponentChanges.builder();

            Item item = itemStackArgument.getItem();
            if (item.getComponents().contains(DataComponentTypes.MAX_DAMAGE) && item != Items.ELYTRA) {
                editor.add(DataComponentTypes.UNBREAKABLE, new UnbreakableComponent(true));
            }
            NbtCompound nbt = customData.copyNbt();
            if (!isBlackListedItem(item)) {
                nbt.putString(
                    ImmortalTrophies.TROPHY_REAL_ITEM_ID_NBT_KEY,
                    Registries.ITEM.getId(item).toString()
                );
                itemStackArgument = new ItemStackArgument(
                    ImmortalTrophies.TROPHY_ITEM.getRegistryEntry(),
                    itemStackArgument.components
                );
            }
            editor.add(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
            stack = itemStackArgument.createStack(amount, checkOverstack);
            stack.applyChanges(editor.build());
        } else {
            stack = itemStackArgument.createStack(amount, checkOverstack);
        }
        return stack;
    }
}
