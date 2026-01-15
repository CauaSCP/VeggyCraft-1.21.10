package net.klayil.veggycraft.item.tabs;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.klayil.veggycraft.VeggyCraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CustomTabsMethods {
    public static HashMap<ResourceKey<CreativeModeTab>, ArrayList<List>> to_add_afters = new HashMap<>();

    public enum ToPutAt {
        BEFORE,
        AFTER
    }

    public static final ToPutAt BEFORE = ToPutAt.BEFORE;
    public static final ToPutAt AFTER = ToPutAt.AFTER;



    public static void addToTab(@NotNull ResourceKey<CreativeModeTab> creativeModeTab, RegistrySupplier<Item> itemToAdd) {
        addToTab(creativeModeTab, null, itemToAdd);
    }

    public static void addToTab(@NotNull ResourceKey<CreativeModeTab> creativeModeTab, @Nullable ToPutAt ENUM, RegistrySupplier<Item> itemToAdd, @Nullable Item... toAddAfters) {
        List<Item> toAddAftersList = new ArrayList<>(Arrays.stream(toAddAfters).toList());
        toAddAftersList.add(null);

        if (toAddAfters.length == 0 | toAddAftersList.getFirst() == null | ENUM == null) {
            CreativeTabRegistry.append(creativeModeTab, itemToAdd);

            return;
        }

        CustomTabsMethods new_one = new CustomTabsMethods();
        new_one.addToTabNonStatic(creativeModeTab, ENUM, itemToAdd, toAddAfters);


        VeggyCraft.LOGGER.info("#Size /\\: %d".formatted(to_add_afters.size()));
    }


    protected void addToTabNonStatic(@NotNull ResourceKey<CreativeModeTab> creativeModeTab, @NotNull ToPutAt ENUM, RegistrySupplier<Item> itemToAdd, @Nullable Item... toAddAfters) {
        if (toAddAfters.length > 0) {
            addListToHashMap(creativeModeTab);

            if (toAddAfters[0] == null) return;

            List listToAdd = List.of(toAddAfters[0], itemToAdd, ENUM);

            to_add_afters.get(creativeModeTab).add(listToAdd);

//            VeggyCraft.LOGGER.info("#Size: %d".formatted(to_add_afters.keySet().size()));
        }
    }

    private static void addListToHashMap(@NotNull ResourceKey<CreativeModeTab> key) {
        if (to_add_afters.containsKey(key)) return;

        to_add_afters.put(key, new ArrayList<>());

    }
}
