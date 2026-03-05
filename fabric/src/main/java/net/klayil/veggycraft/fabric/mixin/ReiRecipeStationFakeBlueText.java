package net.klayil.veggycraft.fabric.mixin;

import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.EntryDefinition;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.impl.common.entry.AbstractEntryStack;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AbstractEntryStack.class, remap = false)
public abstract class ReiRecipeStationFakeBlueText {
//    @Shadow @Final private ResourceLocation id;
//    @Shadow public abstract @Nullable ResourceLocation getIdentifier();

    @Shadow public abstract EntryStack<ItemStack> cheatsAs();

    @Inject(
            method = "getContainingNamespace",
            at = @At("HEAD"),
            cancellable = true
    )
    private void formatting(CallbackInfoReturnable<String> cir) {
        EntryDefinition<?> definition = ((EntryStack<?>) this).getDefinition();

        if (VanillaEntryTypes.ITEM != definition.getType()) return;

        if (this.cheatsAs().getValue().getItem() != new ItemStack(ModItems.THIS_MOD_CLOCK).getItem()) return;

        cir.setReturnValue("minecraft");
    }
}
