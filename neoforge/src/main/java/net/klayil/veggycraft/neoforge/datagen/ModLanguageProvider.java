package net.klayil.veggycraft.neoforge.datagen;

import net.klayil.klay_api.KlayApi;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    final String locale;

    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, KlayApi.MOD_ID, locale);

        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        if (locale.toLowerCase().startsWith("en_")) {
            for (Component translatable : LanguagesTextsEnglish.getTranslatableCodes()) {
                add(translatable.getString(), LanguagesTextsEnglish.get(translatable));
            }

            return;
        }
        if (locale.toLowerCase().startsWith("pt_")) {
            for (Component translatable : LanguagesTextsPortuguese.getTranslatableCodes()) {
                add(translatable.getString(), LanguagesTextsPortuguese.get(translatable));
            }
        }
    }
}