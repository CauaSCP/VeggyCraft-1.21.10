package net.klayil.veggycraft.neoforge.mixin;

import dev.architectury.registry.registries.RegistrySupplier;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.tabs.CustomTabsMethods;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CustomTabsMethods.class)
public class CustomTabsMethodsMixin{

    @Inject(
            method = "addToTabNonStatic",
            at = @At("TAIL")
    )
    protected  void tabs$addToTabNonStatic(@NotNull ResourceKey<CreativeModeTab> creativeModeTab, RegistrySupplier<Item> itemToaAdd, @Nullable Item[] toAddAfters, CallbackInfo ci) {
    }
}
