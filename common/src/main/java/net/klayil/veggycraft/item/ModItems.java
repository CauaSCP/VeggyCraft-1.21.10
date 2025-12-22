package net.klayil.veggycraft.item;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.klayil.klay_api.item.KlayApiModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.food.FoodProperties;

import java.util.function.Supplier;

import static net.klayil.klay_api.item.KlayApiModItems.AllKlayApiItems;
import static net.klayil.veggycraft.VeggyCraft.MOD_ID;
import static net.klayil.veggycraft.item.tabs.VeggyCraftCreativeTabsToGet.REPLACEMENTS;

public class ModItems {
    private static String currentItemName;
    public static RegistrySupplier<Item> THIS_MOD_FLOUR;

    public static RegistrySupplier[] VeggyMeats;

//    Items.POTION

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

        final FoodProperties SEITAN_COOKED_BEEF_PROPS = new FoodProperties(5 * 2, 0.97f, false);

        final RegistrySupplier<Item> seitanCookedBeef = KlayApiModItems.createItem("seitan_cooked_beef", REPLACEMENTS, () -> KlayApiModItems.baseProperties("seitan_cooked_beef", MOD_ID).food(SEITAN_COOKED_BEEF_PROPS), MOD_ID);

        currentItemName = "08_items_stacked_of_flour";
        createDamageableItem(currentItemName, 1, 65);

        for (int index = 16; index <= 64; index += 8) {
            currentItemName = "%d_items_stacked_of_flour".formatted(index);

            createDamageableItem(currentItemName, 1, 65);
        }

        VeggyMeats = new RegistrySupplier[]{seitanCookedBeef};

        ITEMS.register();
    }
}
