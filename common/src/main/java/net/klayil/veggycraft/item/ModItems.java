package net.klayil.veggycraft.item;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.klayil.klay_api.item.KlayApiModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.function.Supplier;

import static net.klayil.klay_api.item.KlayApiModItems.AllKlayApiItems;
import static net.klayil.veggycraft.VeggyCraft.MOD_ID;

public class ModItems {
    private static String currentItemName;
    public static RegistrySupplier<Item> THIS_MOD_FLOUR;

    public static DeferredRegister<Item> ITEMS = KlayApiModItems.createItemsRegister(MOD_ID);

    private static void createDamageableItem(String itemId, int maxStackSize, int maxDamage) {
        ResourceLocation itemLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, itemId);

        Supplier<Item> item =
            () -> new UnrepairableDamageableItems(KlayApiModItems.baseProperties(itemId, MOD_ID).stacksTo(maxStackSize), maxDamage);

        AllKlayApiItems.put(itemLocation.toString(), ITEMS.register(itemLocation, item));
    }

    public static void initItems() {
        final String flourName = "wheat_flour";
        THIS_MOD_FLOUR = KlayApiModItems.createItem(flourName, CreativeModeTabs.INGREDIENTS, () -> KlayApiModItems.baseProperties(flourName, MOD_ID).stacksTo(8), MOD_ID);

        currentItemName = "08_items_stacked_of_flour";
        createDamageableItem(currentItemName, 1, 65);

        for (int index = 16; index <= 64; index += 8) {
            currentItemName = "%d_items_stacked_of_flour".formatted(index);

            createDamageableItem(currentItemName, 1, 65);
        }

        ITEMS.register();
    }
}
