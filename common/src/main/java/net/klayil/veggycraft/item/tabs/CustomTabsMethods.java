package net.klayil.veggycraft.item.tabs;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.component.DataComponents;

import java.util.*;
import java.util.function.Supplier;

public class CustomTabsMethods {
    public boolean isDataGen() {
        return VeggyCraft.isDataGen;
    }

    public static class ItemStackList extends java.util.ArrayList<ItemStack> {
        static abstract class SupBool implements Supplier<Boolean> {
            abstract public String type();
        }

        int size;
        transient Object[] elementData;

        static SupBool runError;

        @SafeVarargs
        private <L extends List<O>, O>ItemStackList(L... lists) {
            super(master(lists[0].getFirst(), lists[0].get(1)));

            assert size() > 0;
        }

        public <L extends List<O>, O>ItemStackList(L list) {
            this(list, new ArrayList<>());

            ItemStackList res = this;

            if (runError != null) if (runError.get()) {
                VeggyCraft.LOGGER.info("error: incompatible types: %s cannot be converted to ItemStack".formatted(runError.type()));
            }
            else {

                if (
                        res.toArray()[0] instanceof ItemStack resItem1
                                && res.toArray()[1] instanceof ItemStack resItem2
                ) {
                    elementData = new ItemStack[]{resItem1, resItem2};
                    size = 2;
                }
            }
        }

        private static <I, S> List<ItemStack> master(S itemObj1, I itemObj2) {
            if (itemObj1 instanceof ItemStack itemParam1 && itemObj2 instanceof RegistrySupplier<?> itemParamSup2) {
                Item itemNum2 = (Item) itemParamSup2.get();

                return List.of(itemParam1, new ItemStack(itemNum2));
            }

            VeggyCraft.LOGGER.error("#itemObj2 >> %s", itemObj2.toString());

            runError = new SupBool() {
                @Override
                public String type() {
                    return itemObj1.getClass().getName();
                }

                @Override
                public Boolean get() {
                    return true;
                }
            };

            return List.of();
        }
    }

    public enum ToPutAt {
        BEFORE,
        AFTER
    }

    public static final ToPutAt BEFORE = ToPutAt.BEFORE;
    public static final ToPutAt AFTER = ToPutAt.AFTER;



    public static void addToTab(@NotNull ResourceKey<CreativeModeTab> creativeModeTab, RegistrySupplier<Item> itemToAdd) {
        addToTab(creativeModeTab, null, itemToAdd);
    }

    public static void addToTab(@NotNull ResourceKey<CreativeModeTab> creativeModeTab, @Nullable ToPutAt ENUM, RegistrySupplier<Item> itemToAdd, @Nullable Item toAddAfter) {
        @Nullable ItemStack stack = null;

        if (toAddAfter != null) {
            stack = new ItemStack(toAddAfter);
        }

        addToTab(creativeModeTab, ENUM, itemToAdd, stack);
    }

    public static HashMap<ResourceKey<CreativeModeTab>, ArrayList<List<?>>> to_add_afters = new HashMap<>();

    public static void addToTab(@NotNull ResourceKey<CreativeModeTab> creativeModeTab, @Nullable ToPutAt ENUM, RegistrySupplier<Item> itemToAdd, @Nullable ItemStack... toAddAfters) {
        List<ItemStack> toAddAftersList = new ArrayList<>(Arrays.stream(toAddAfters).toList());
        toAddAftersList.add(null);

        if (toAddAfters.length == 0 || toAddAftersList.getFirst() == null || ENUM == null) {
            CreativeTabRegistry.append(creativeModeTab, itemToAdd);

            return;
        }

        CustomTabsMethods new_one = new CustomTabsMethods();
        new_one.addToTabNonStatic(creativeModeTab, ENUM, itemToAdd, toAddAfters);
    }

    private void forEach(Collection<ItemStack> displayItemsCollection, Integer[] stacksCount, ItemStack[] toAddAfters) {
        ItemStack[] firstInstanceStack = new ItemStack[1];

        for (ItemStack otherStack: displayItemsCollection) {
            if (otherStack.is(Items.SPLASH_POTION)) {
                PotionContents contents = otherStack.get(DataComponents.POTION_CONTENTS);
                assert contents != null;
                contents.potion().ifPresent(potionEntry -> {
                    if (Objects.requireNonNull(potionEntry.value().name().toLowerCase()).equals("mundane")) {
                        ModItems.MUNDANE_SPLASH_POTION_ITEM_STACKS.add(otherStack);
                    }
                });
            }

            if (stacksCount[0] >= 2 && ModItems.MUNDANE_SPLASH_POTION_ITEM_STACKS.size() > 1) break;

            if (!(ItemStack.isSameItem(otherStack, toAddAfters[0]))) continue;

            stacksCount[0] = stacksCount[0] + 1;
            if (stacksCount[0] == 1) firstInstanceStack[0] = otherStack;
        }

        if (stacksCount[0] >= 2) toAddAfters[0] = firstInstanceStack[0];
    }

    protected void addToTabNonStatic(@NotNull ResourceKey<CreativeModeTab> creativeModeTab, @NotNull ToPutAt ENUM, RegistrySupplier<Item> itemToAdd, @Nullable ItemStack... toAddAfters) {
        if (toAddAfters.length > 0) { try {
            addListToHashMap(creativeModeTab);

            if (toAddAfters[0] == null) {

                throw new IndexOutOfBoundsException("Index 0 of toAddAfters is null");
//                return;

            }

            final Integer[] stacksCount = {0};
//            final ItemStack[] firstInstanceStack = new ItemStack[1];

            CreativeModeTab toGetDisplayItems = Objects.requireNonNull(BuiltInRegistries.CREATIVE_MODE_TAB.getValue(creativeModeTab));

            if (isDataGen()) return;

            Collection<ItemStack> displayItems;
            displayItems = toGetDisplayItems.getDisplayItems();
            if (displayItems.isEmpty()) {
                CreativeModeTab.ItemDisplayParameters params = new CreativeModeTab.ItemDisplayParameters(
                        FeatureFlags.DEFAULT_FLAGS,
                        false,
                        VanillaRegistries.createLookup()
                );

                toGetDisplayItems.buildContents(params);

                displayItems = toGetDisplayItems.getDisplayItems();
            }

            forEach(displayItems, stacksCount, toAddAfters);

            List<?> listToAdd = List.of(toAddAfters[0], itemToAdd, ENUM);

            to_add_afters.get(creativeModeTab).add(listToAdd);

//            VeggyCraft.LOGGER.info("#Size: %d".formatted(to_add_afters.size())); /// for some reason never happens
        } catch (IndexOutOfBoundsException ex) {
            throw ex;
        } catch (Exception e) {
            if (e.getMessage() == null) {
                throw e;
            }

            if (!e.getMessage().toLowerCase().contains("registry object not present")) {
                throw e;
            }
        }
        }
    }

    private static void addListToHashMap(@NotNull ResourceKey<CreativeModeTab> key) {
        if (to_add_afters.containsKey(key)) return;

        to_add_afters.put(key, new ArrayList<>());

    }
}
