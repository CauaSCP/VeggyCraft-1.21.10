package net.klayil.veggycraft.neoforge.datagen;

import net.minecraft.network.chat.Component;

import java.util.*;
import java.util.function.Supplier;

public class LanguagesTextsPortuguese extends LanguagesTextsDetailed {
    LanguagesTextsPortuguese() {
        super();

        Supplier<HashMap<String, String>> ssp = () -> {
            setTexts("klay_api.veggycraft.replacements_tab", "Substituições de itens de mobs",
                    "architectury.klay_api.flour_proto_bundle", "Saco de Farinha de Trigo",
                    "item.veggycraft.wheat_flour", "Farinha de Trigo",
                    "item.veggycraft.seitan_cooked_beef", "Carne de Seitan Cozida",
                    "klay_api.description.flour_proto_bundle", "Hand-use this bundle to retrieve the flours!",

                    "item.veggycraft.carbon_prefix", "",
                    "item.veggycraft.carbon_space_1", "",
                    "item.veggycraft.carbon_space_2", " ",
                    "item.veggycraft.carbon_suffix", "de Carbono",


                    "klay_api.veggycraft.carbon_dying_tab", "Process. de corante por carb.",
                    "item.veggycraft.coal_carbon_cutter", "Cortador de Carbono (Carvão)",
                    "item.veggycraft.diamond_carbon_cutter", "Cortador de Carbono (Diamante)",
                    "item.veggycraft.black_of_coal_carbon", "Carbono Impuro de Carvão",
                    "item.veggycraft.shiny_of_diamond_coal_carbon", "Carbono Mestiço e Brilhoso de Carvão e Diamante",
                    "item.veggycraft.dry_raw_seitan", "Seitan Cru Seco",
                    "item.veggycraft.wet_raw_seitan", "Seitan Cru Cozinhável (Molhado)",


                    "veggycraft_overrides", "VeggyCraft Overrides",
                    "klay_api.smash.predicate", "Esmague com o",
                    "klay_api.smash.wan", "!",

                    "item.veggycraft.modal", "Block de tecido modal de cor ",
                    "suffix.veggycraft.modal", "",


                    "veggycraft.woods.carnauba", "Carnaúba",

                    "item.veggycraft.molasses_block", "Bloco de Melado",
                    "item.veggycraft.carnauba_powder", "Pó de Carnaúba",
                    "item.veggycraft.chopped_apple", "Maçã Picada",
                    "item.veggycraft.apple_sauce", "Purê de Maçã",
                    "item.veggycraft.brown_sugar", "Açúcar Mascavo",
                    "item.veggycraft.straw_bed", "Cama de Palha",
                    "item.veggycraft.birch_pulp_modal", "Polpa de Modal de Bétula",

                    "wax.prefix", "Cera de ",
                    "wax.suffix", "",


                    "veggycraft.woods.prefix.carnauba_log", "Tronco de ",
                    "veggycraft.woods.prefix.carnauba_wood", "Madeira de ",
                    "veggycraft.woods.prefix.stripped_log", "Tronco de ",
                    "veggycraft.woods.prefix.stripped_wood", "Madeira de ",
                    "veggycraft.woods.prefix.carnauba_leaves", "Folhas de ",
                    "veggycraft.woods.prefix.carnauba_sapling", "Muda de ",


                    "even_stripped.prefix", "",
                    "even_stripped.suffix", " Ainda Mais Descascado",


                    "veggycraft.woods.suffix.carnauba_log", "",
                    "veggycraft.woods.suffix.carnauba_wood", "",
                    "veggycraft.woods.suffix.stripped_log", " Descascado",
                    "veggycraft.woods.suffix.stripped_wood", " Descascada",
                    "veggycraft.woods.suffix.carnauba_leaves", "",
                    "veggycraft.woods.suffix.carnauba_sapling", "",

                    "klay_api.smash.wheat", "Me esmague com um pistão!",
                    "klay_api.description.pulp", "Não é o que parece!",

                    "item.veggycraft.molasses_bottle", "Garrafa de Melado",
                    "item.veggycraft.sugar_bag", "Saco de Açúcar",

                    "item.veggycraft.brown_sugar_in_bottle", "Melado \"Mascávo\"",

                    "klay_api.have.to.wait", "Pera!, calma lá...",

                    "klay_api.wait.for", "Espere por",

                    "klay_api.hours.prefix", "",
                    "klay_api.minutes.prefix", "",
                    "klay_api.seconds.prefix", "",

                    "klay_api.hours.suffix", " hora(s)",
                    "klay_api.minutes.suffix", " minuto(s)",
                    "klay_api.seconds.suffix", " segundos(s)",

                    "master.inFloor", "(Com Item Dropado no Chão)"
            );

            return texts;
        };

        ssp.get();
    }
}
