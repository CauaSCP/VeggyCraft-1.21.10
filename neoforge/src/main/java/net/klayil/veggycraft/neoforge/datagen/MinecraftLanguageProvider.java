package net.klayil.veggycraft.neoforge.datagen;

import net.klayil.veggycraft.VeggyCraft;
import net.minecraft.data.PackOutput;
import net.minecraft.locale.Language;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.List;

public class MinecraftLanguageProvider extends LanguageProvider {
    final String locale;

    public MinecraftLanguageProvider(PackOutput output, String locale) {
        super(output,"minecraft", locale);

        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        if (locale.toLowerCase().startsWith("en_")) return;
        if (locale.toLowerCase().startsWith("pt_")) return;

//        Language.getInstance().getOrDefault()

        List<String> colours = List.of(
      "color.minecraft.black",
                "color.minecraft.blue",
                "color.minecraft.brown",
                "color.minecraft.cyan",
                "color.minecraft.gray",
                "color.minecraft.green",
                "color.minecraft.light_blue",
                "color.minecraft.light_gray",
                "color.minecraft.lime",
                "color.minecraft.magenta",
                "color.minecraft.orange",
                "color.minecraft.pink",
                "color.minecraft.purple",
                "color.minecraft.red",
                "color.minecraft.white",
                "color.minecraft.yellow"
        );

        for (String colour : colours) {
            add(colour, Language.getInstance().getOrDefault(colour));
        }
    }
}