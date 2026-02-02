package net.klayil.veggycraft.neoforge.datagen;

import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class LanguagesTextsEnglish extends LanguagesTextsDetailed {
    public LanguagesTextsEnglish() {
        super();

        Supplier<HashMap<String, String>> ssp = () -> {
            setTexts(
                    "klay_api.veggycraft.replacements_tab", "Mobs Drops Replacements",
                    "architectury.klay_api.flour_proto_bundle", "Flour-Filled Wheat Bundle",
                    "item.veggycraft.wheat_flour", "Wheat Flour",
                    "item.veggycraft.seitan_cooked_beef", "Cooked Seitan",
                    "veggycraft_overrides", "VeggyCraft Overrides",
                    "klay_api.description.flour_proto_bundle", "Hand-use this bundle to retrieve the flours!",


                    "item.veggycraft.carbon_prefix", "Carbon",
                    "item.veggycraft.carbon_space_1", " ",
                    "item.veggycraft.carbon_space_2", "",
                    "item.veggycraft.carbon_suffix", "",


                    "klay_api.veggycraft.carbon_dying_tab", "Dye processing from carbon",
                    "item.veggycraft.coal_carbon_cutter", "Carbon Cutter (Coal)",
                    "item.veggycraft.diamond_carbon_cutter", "Carbon Cutter (Diamond)",
                    "item.veggycraft.black_of_coal_carbon", "Black of Coal Carbon",
                    "item.veggycraft.shiny_of_diamond_coal_carbon", "Shiny of Diamond Coal Carbon",
                    "item.veggycraft.dry_raw_seitan", "Dry Raw Seitan",
                    "item.veggycraft.wet_raw_seitan", "Wet Raw Seitan (Cookable)",


                    "klay_api.smash.predicate", "",
                    "klay_api.smash.wan", " Smashin'!",

                    "item.veggycraft.modal", "",
                    "suffix.veggycraft.modal", " Modal",


                    "veggycraft.woods.carnauba", "Carnauba",

                    "item.veggycraft.molasses_block", "Molasses Block",
                    "item.veggycraft.carnauba_powder", "Carnauba Powder",
                    "item.veggycraft.chopped_apple", "Chopped Apple",
                    "item.veggycraft.apple_sauce", "Applesauce",
                    "item.veggycraft.brown_sugar", "Brown Sugar",
                    "item.veggycraft.straw_bed", "Straw Bed",
                    "item.veggycraft.birch_pulp_modal", "Birch Modal Pulp",

                    "wax.prefix", "",
                    "wax.suffix", " Wax",


                    "veggycraft.woods.prefix.carnauba_log", "",
                    "veggycraft.woods.prefix.carnauba_wood", "",
                    "veggycraft.woods.prefix.stripped_log", "Stripped ",
                    "veggycraft.woods.prefix.stripped_wood", "Stripped ",
                    "veggycraft.woods.prefix.carnauba_leaves", "",
                    "veggycraft.woods.prefix.carnauba_sapling", "",

                    "even_stripped.prefix", "Even Stripped ",
                    "even_stripped.suffix", "",


                    "item.veggycraft.brown_sugar_in_bottle", "Dried Molasses",


                    "veggycraft.woods.suffix.carnauba_log", " Log",
                    "veggycraft.woods.suffix.carnauba_wood", " Wood",
                    "veggycraft.woods.suffix.stripped_log", " Log",
                    "veggycraft.woods.suffix.stripped_wood", " Wood",
                    "veggycraft.woods.suffix.carnauba_leaves", " Leaves",
                    "veggycraft.woods.suffix.carnauba_sapling", " Sapling",

                    "klay_api.smash.wheat", "Smash me with a piston!",
                    "klay_api.description.pulp", "It isn't what it looks like!",

                    "item.veggycraft.molasses_bottle", "Molasses Bottle",
                    "item.veggycraft.sugar_bag", "Sugar Bag",


                    "klay_api.have.to.wait", "Wait!, hold on...",

                    "klay_api.wait.for", "Wait for",

                    "klay_api.hours.prefix", "",
                    "klay_api.minutes.prefix", "",
                    "klay_api.seconds.prefix", "",

                    "klay_api.hours.suffix", " hour(s)",
                    "klay_api.minutes.suffix", " minute(s)",
                    "klay_api.seconds.suffix", " second(s)",

                    "master.inFloor", "(Dropped in Floor)"
            );

            return texts;
        };

        ssp.get();
    }
}
