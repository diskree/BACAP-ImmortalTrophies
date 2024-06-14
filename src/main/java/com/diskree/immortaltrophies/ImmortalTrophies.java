package com.diskree.immortaltrophies;

import net.fabricmc.api.ModInitializer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class ImmortalTrophies implements ModInitializer {

    public static final Item TROPHY_ITEM = new Item(new Item.Settings());

    public static final String TROPHY_NBT_KEY = "Trophy";
    public static final String TROPHY_REAL_ITEM_ID_NBT_KEY = "TrophyRealItemId";

    public static boolean isTrophy(Entity entity) {
        return entity instanceof ItemEntity itemEntity && isTrophy(itemEntity.getStack());
    }

    public static boolean isTrophy(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        return isTrophy(stack.get(DataComponentTypes.CUSTOM_DATA));
    }

    public static boolean isTrophy(NbtComponent nbt) {
        if (nbt == null) {
            return false;
        }
        return nbt.getNbt().getBoolean(TROPHY_NBT_KEY);
    }

    public static @Nullable ItemStack getRealItem(ItemStack stack) {
        if (stack == null) {
            return null;
        }
        NbtComponent nbt = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (nbt == null || !nbt.contains(ImmortalTrophies.TROPHY_REAL_ITEM_ID_NBT_KEY)) {
            return stack;
        }
        Identifier identifier = Identifier.of(nbt.getNbt().getString(ImmortalTrophies.TROPHY_REAL_ITEM_ID_NBT_KEY));
        ItemStack itemStack = new ItemStack(Registries.ITEM.get(identifier));
        itemStack.applyComponentsFrom(stack.getComponents());
        return itemStack;
    }

    public static void teleportTrophyToSafeZone(Entity entity) {
        if (!isTrophy(entity)) {
            return;
        }
        World world = entity.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            entity.teleport(serverWorld, 0.5, entity.getWorld().getSpawnPos().getY(), 0.5, Set.of(), entity.getYaw(), entity.getPitch());
            entity.setVelocity(Vec3d.ZERO);
        }
    }

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM, Identifier.of(BuildConfig.MOD_ID, "trophy"), TROPHY_ITEM);
    }
}
