package net.klayil.veggycraft.recipe;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.component.ModDataComponentTypes;
import net.klayil.veggycraft.item.ModItems;
import net.klayil.veggycraft.item.RepairableItemsExtension;
import net.klayil.veggycraft.item.UnrepairableDamageableItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.Objects;

public final class SharedCraftHandler {
    private SharedCraftHandler() {}


    public static ItemStack checkKeeps(ItemStack stack, ItemStack resultStack) {
        Item inputItem = stack.getItem();


        Item remainder = inputItem.getCraftingRemainder().getItem();
        ItemStack replace = new ItemStack(remainder);


        if (!new ItemStack(remainder).isEmpty()) {
            return replace;
        }
//            remaindersCountBit += 2 << i;

//            if ((remaindersCountBit & (2 << i)) != 0) {
//                VeggyCraft.LOGGER.info("NOT doing #Slot %d".formatted(i));
//                continue;
//            }
//            VeggyCraft.LOGGER.info("doing #Slot %d".formatted(i));

        if (stack.isEmpty()) return ItemStack.EMPTY;

        if (
                ItemStack.isSameItem(resultStack, new ItemStack(ModItems.THIS_MOD_FLOUR))
                        && ItemStack.isSameItem(stack, new ItemStack(Items.PISTON))
        ) {
            ItemStack pistonRemainder = stack.copy();
            pistonRemainder.grow(1);
            return pistonRemainder;
        }

        if (
                !ItemStack.isSameItem(resultStack, new ItemStack(ModItems.THIS_MOD_FLOUR))
                        && ItemStack.isSameItem(stack, new ItemStack(ModItems.FLOUR_BAG))
        ) {
            stack.setCount(0);
            return stack;
        } else if (
                ItemStack.isSameItem(resultStack, new ItemStack(ModItems.THIS_MOD_FLOUR))
                        && ItemStack.isSameItem(stack, new ItemStack(ModItems.FLOUR_BAG))
                        && stack.has(ModDataComponentTypes.HEALTH.get())
        ) {
            int bagSize = Objects.requireNonNull(stack.get(ModDataComponentTypes.HEALTH.get())).value();


            if (bagSize >= 16) {
                VeggyCraft.LOGGER.info("#bagSize: %d".formatted(bagSize));

                ItemStack recipeRemainder = new ItemStack(
                        ModItems.FLOUR_BAG
                );

                ModDataComponentTypes.ItemHealth bfrHealth = stack.get(ModDataComponentTypes.HEALTH.get());

                recipeRemainder.set(
                        ModDataComponentTypes.HEALTH.get(),
                        new ModDataComponentTypes.ItemHealth(Objects.requireNonNull(bfrHealth).value() - 8, bfrHealth.max())
                );

                return recipeRemainder;
            }

            stack.setCount(0);

            return stack;
        }

        return ItemStack.EMPTY;
    }

    public static void onCraft(CraftingContainer grid, ItemStack result) {
//        int remaindersCountBit = 0;
        for (int i = 0; i < grid.getContainerSize(); i++) {
            if (grid.getItem(i).isEmpty()) continue;
            ItemStack itemStack = grid.getItem(i);

            ItemStack toChange = checkKeeps(itemStack, result);

            if (!toChange.is(itemStack.getItem().getCraftingRemainder().getItem())) {
                grid.setItem(i, toChange);
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
