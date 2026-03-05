package net.klayil.veggycraft.mixin;

import net.klayil.veggycraft.mixin.others.LoggingCreativeModeTab;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Set;

@Mixin(CreativeModeTab.class)
public class VanillaCreativeModeTabMixin {
    @Shadow @Final private CreativeModeTab.DisplayItemsGenerator displayItemsGenerator;

    @Shadow private Collection<ItemStack> displayItems;

    @Shadow private Set<ItemStack> displayItemsSearchTab;

    @Inject(
            method = "buildContents",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onBuildContents(CreativeModeTab.ItemDisplayParameters parameters, CallbackInfo ci) {
        CreativeModeTab tab = (CreativeModeTab) (Object) this;

        CreativeModeTab.ItemDisplayBuilder itemDisplayBuilder = new CreativeModeTab.ItemDisplayBuilder(tab, parameters.enabledFeatures);

        BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(tab).orElseThrow(() -> new IllegalStateException("Unregistered creative tab: " + tab.toString()));


        LoggingCreativeModeTab.DisplayItemsGenerator displayItemsGenerator = new LoggingCreativeModeTab.DisplayItemsGenerator(this.displayItemsGenerator);

//        displayItemsGenerator.
//        this.

        displayItemsGenerator.accept(parameters, itemDisplayBuilder);

        this.displayItems  = itemDisplayBuilder.tabContents;
        this.displayItemsSearchTab  = itemDisplayBuilder.searchTabContents;

        ci.cancel();
    }
}
