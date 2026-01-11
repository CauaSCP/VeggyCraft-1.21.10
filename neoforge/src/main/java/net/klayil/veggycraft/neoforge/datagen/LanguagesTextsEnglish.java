package net.klayil.veggycraft.neoforge.datagen;

import net.minecraft.network.chat.Component;

import java.util.*;

public class LanguagesTextsEnglish {
    public static String get(Component translatableCode) {
        String key = translatableCode.getString();

        if (texts3.containsKey(key)) return texts3.get(key);
        if (texts4.containsKey(key)) return texts4.get(key);
        if (texts2.containsKey(key)) return texts2.get(key);
        if (texts1.containsKey(key)) return texts1.get(key);

        throw new NoSuchElementException("No result for translatable code `%s` found!".formatted(key));
    }

    public static List<Component> getTranslatableCodes() {
        ArrayList<Component> list = new ArrayList<>(getListFromMap(texts3));

        list.addAll(getListFromMap(texts2));
        list.addAll(getListFromMap(texts1));
        list.addAll(getListFromMap(texts4));

        return list;
    }

    private static List<Component> getListFromMap(Map<String, String>map) {
        return map.keySet().stream().map(
                (str) -> { return ( (Component) Component.translatable(str) ); }
        ).toList();
    }

    final private static Map<String, String> texts1 = Map.of(
            "klay_api.veggycraft.replacements_tab", "Mobs Drops Replacements",
            "architectury.klay_api.flour_proto_bundle", "Flour-Filled Wheat Bundle",
            "item.veggycraft.wheat_flour", "Wheat Flour",
            "item.veggycraft.seitan_cooked_beef", "Cooked Seitan",
            "veggycraft_overrides", "VeggyCraft Overrides",
            "klay_api.description.flour_proto_bundle", "Hand-use this bundle to retrieve the flours!",

            "item.veggycraft.carbon_prefix", "Carbon",
            "item.veggycraft.carbon_space_1", " ",
            "item.veggycraft.carbon_space_2", "",
            "item.veggycraft.carbon_suffix", ""
    );

    final static private Map<String, String> texts2 = Map.of(
            "klay_api.veggycraft.carbon_dying_tab", "Dye processing from carbon",
            "item.veggycraft.coal_carbon_cutter", "Carbon Cutter (Coal)",
            "item.veggycraft.diamond_carbon_cutter", "Carbon Cutter (Diamond)",
            "item.veggycraft.black_of_coal_carbon", "Black of Coal Carbon",
            "item.veggycraft.shiny_of_diamond_coal_carbon", "Shiny of Diamond Coal Carbon",
            "item.veggycraft.dry_raw_seitan", "Dry Raw Seitan",
            "item.veggycraft.wet_raw_seitan", "Wet Raw Seitan (Cookable)"
    );

    final static private Map<String, String> texts3 = Map.of(
            "klay_api.smash.predicate", "",
            "klay_api.smash.wan", " Smashin'!",

            "item.veggycraft.modal", "Modal Fabric Block of",
            "suffix.veggycraft.modal", " Color"
    );

    final static private Map<String, String> texts4 = Map.of(
            "item.veggycraft.molasses_block", "Molasses Block"
    );
}
