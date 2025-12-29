package net.klayil.veggycraft.recipe;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.component.ModDataComponentTypes;
import net.klayil.veggycraft.item.ModItems;
import net.klayil.veggycraft.item.RepairableItemsExtension;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public final class SharedCraftHandler {
    private SharedCraftHandler() {}

    public static void onCraft(CraftingContainer grid, ItemStack result) {
        for (int i = 0; i < grid.getContainerSize(); i++) {
            ItemStack stack = grid.getItem(i);

            if (stack.isEmpty()) continue;

            if (result == null) continue;

            if (
                    !ItemStack.isSameItem(result, new ItemStack(ModItems.THIS_MOD_FLOUR))
                    && BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath().endsWith("_items_stacked_of_flour")
            ) {
                grid.clearContent();
            } else if (
                    ItemStack.isSameItem(result, new ItemStack(ModItems.THIS_MOD_FLOUR))
                    && BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath().endsWith("_items_stacked_of_flour")
                    && stack.has(ModDataComponentTypes.HEALTH.get())
            ) {
                int bagSize = Objects.requireNonNull(stack.get(ModDataComponentTypes.HEALTH.get())).value();


                if (bagSize >= 16) {
                    VeggyCraft.LOGGER.info("#bagSize: %d".formatted(bagSize));

                    ItemStack recipeRemainder = new ItemStack(
                        BuiltInRegistries.ITEM.getValue(
                            ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "%02d_items_stacked_of_flour".formatted(bagSize - 8))
                        )
                    );

                    ModDataComponentTypes.ItemHealth bfrHealth = stack.get(ModDataComponentTypes.HEALTH.get());

                    recipeRemainder.set(
                            ModDataComponentTypes.HEALTH.get(),
                            new ModDataComponentTypes.ItemHealth(Objects.requireNonNull(bfrHealth).value() - 8, bfrHealth.max())
                    );

                    grid.setItem(i, recipeRemainder);

                    return;
                }

                grid.clearContent();
            }

            if (
                    ItemStack.isSameItem(stack, result)
                    | (stack.getItem() instanceof RepairableItemsExtension repairableItem && repairableItem.getRepairItem() == null)
            ) {
                grid.clearContent();
            }
        }
    }

    public static boolean shouldBlockCraft(CraftingContainer grid) {
        ItemStack itemOfBefore = ItemStack.EMPTY;

        for (int i = 0; i < grid.getContainerSize(); i++) {
            ItemStack stack = grid.getItem(i);

            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof RepairableItemsExtension repairableItem && repairableItem.getRepairItem() == null) {
                if (ItemStack.isSameItem(itemOfBefore, stack)) return true;

                itemOfBefore = stack;
            }
        }

        return false;
    }
}
