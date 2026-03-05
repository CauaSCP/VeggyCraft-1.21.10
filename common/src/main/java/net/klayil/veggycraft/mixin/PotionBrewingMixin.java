package net.klayil.veggycraft.mixin;

import net.klayil.veggycraft.ListToLoopIn;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(PotionBrewing.class)
public class PotionBrewingMixin {
    @Inject(method = "isIngredient", at = @At("HEAD"), cancellable = true)
    private void handleCustomItemStackBrewing(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!stack.is(Items.KELP))
            return;

        cir.setReturnValue(true);
    }

    @Inject(method = "mix", at = @At("HEAD"), cancellable = true)
    private void handleCustomItemStackBrewing(ItemStack reagent, ItemStack input, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack result = null;

        if (input.is(Items.POTION) && reagent.is(Items.KELP))
            if (Objects.requireNonNull(input.get(DataComponents.POTION_CONTENTS)).is(Potions.WATER)) {
                result = new ItemStack(ModItems.ALGAE_EXTRACT);
            }

        ListToLoopIn<ItemStack, ItemStack> toLoopIn = new ListToLoopIn<>(ModItems.MUNDANE_SPLASH_POTION_ITEM_STACKS);

        if (result != null) {
            cir.setReturnValue(result);

            return;
        }

        if (!reagent.is(Items.SUGAR))
            return;

        toLoopIn.forLoop(
   mundaneSplashStack -> {
                if (Objects.requireNonNull(mundaneSplashStack.get(DataComponents.POTION_CONTENTS)).potion().isEmpty()) toLoopIn.doBreak();

                if (ItemStack.isSameItemSameComponents(input, mundaneSplashStack)) {
                    toLoopIn.runReturn(new ItemStack(ModItems.OTHER_SPLASH_POTION.get()));

                    toLoopIn.doBreak();
                }
            },

            cir
        );
    }

    @Inject(method = "hasMix", at = @At("HEAD"), cancellable = true)
    private void allowCustomBrewingToStart(ItemStack bottomSlot, ItemStack topSlot, CallbackInfoReturnable<Boolean> cir) {
        if (bottomSlot.is(Items.POTION) && topSlot.is(Items.KELP))
            if (Objects.requireNonNull(bottomSlot.get(DataComponents.POTION_CONTENTS)).is(Potions.WATER)) {
                cir.setReturnValue(true);
                return;
            }

        if (!topSlot.is(Items.SUGAR))
            return;

        ListToLoopIn<ItemStack, Boolean> toLoopIn = new ListToLoopIn<>(ModItems.MUNDANE_SPLASH_POTION_ITEM_STACKS);

        toLoopIn.forLoop(
                mundaneSplashStack -> {
                    if (Objects.requireNonNull(mundaneSplashStack.get(DataComponents.POTION_CONTENTS)).potion().isEmpty()) toLoopIn.doBreak();

                    if (ItemStack.isSameItemSameComponents(bottomSlot, mundaneSplashStack)) {
                        toLoopIn.runReturn(true);

                        toLoopIn.doBreak();
                    }
                },

                cir
        );
    }
}
