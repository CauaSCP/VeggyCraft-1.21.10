package net.klayil.veggycraft.neoforge.datagen;

import net.minecraft.network.chat.Component;

import java.util.*;

public class LanguagesTextsPortuguese {
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

    private final static Map<String, String> texts1 = Map.of(
            "klay_api.veggycraft.replacements_tab", "Substituições de itens de mobs",
            "architectury.klay_api.flour_proto_bundle", "Saco de Farinha de Trigo",
            "item.veggycraft.wheat_flour", "Farinha de Trigo",
            "item.veggycraft.seitan_cooked_beef", "Carne de Seitan Cozida",
            "klay_api.description.flour_proto_bundle", "Hand-use this bundle to retrieve the flours!",

            "item.veggycraft.carbon_prefix", "",
            "item.veggycraft.carbon_space_1", "",
            "item.veggycraft.carbon_space_2", " ",
            "item.veggycraft.carbon_suffix", "de Carbono"
    );

    final static private Map<String, String> texts2 = Map.of(
            "klay_api.veggycraft.carbon_dying_tab", "Process. de corante por carb.",
            "item.veggycraft.coal_carbon_cutter", "Cortador de Carbono (Carvão)",
            "item.veggycraft.diamond_carbon_cutter", "Cortador de Carbono (Diamante)",
            "item.veggycraft.black_of_coal_carbon", "Carbono Impuro de Carvão",
            "item.veggycraft.shiny_of_diamond_coal_carbon", "Carbono Mestiço e Brilhoso de Carvão e Diamante",
            "item.veggycraft.dry_raw_seitan", "Seitan Cru Seco",
            "item.veggycraft.wet_raw_seitan", "Seitan Cru Cozinhável (Molhado)"
    );

    final static private Map<String, String> texts3 = Map.of(
            "veggycraft_overrides", "VeggyCraft Overrides",
            "klay_api.smash.predicate", "Esmague com o",
            "klay_api.smash.wan", "!",

            "item.veggycraft.modal", "Block de tecido modal de cor",
            "suffix.veggycraft.modal", ""
    );

    final static private Map<String, String> texts4 = Map.of(
            "item.veggycraft.molasses_block", "Bloco de Melado"
    );
}
