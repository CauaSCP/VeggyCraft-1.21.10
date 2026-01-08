package net.klayil.veggycraft;

//import net.klayil.veggycraft.compat.recipe.ModRecipes;
import net.klayil.veggycraft.component.ModDataComponentTypes;
import net.klayil.veggycraft.item.ModItems;

import net.klayil.veggycraft.item.tabs.VeggyCraftTabsCode;
import net.klayil.veggycraft.recipe.ModRecipes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class VeggyCraft {
    public static final String MOD_ID = "veggycraft";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
//        ModRecipes.init();

        ModRecipes.register();

        ModDataComponentTypes.registerDataComponentTypes();

        VeggyCraftTabsCode.initTabs();
        ModItems.initItems();

        VeggyCraftTabsCode.initAfterItems.run();
    }
}
