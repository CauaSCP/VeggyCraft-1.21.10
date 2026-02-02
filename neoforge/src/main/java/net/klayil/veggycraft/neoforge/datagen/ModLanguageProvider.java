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

        if ("%s".formatted(locale.toLowerCase()).equals("en_us")) {
            LanguagesTextsEnglish langTextsEnglish = new LanguagesTextsEnglish();

            for (Component translatable : langTextsEnglish.getTranslatableCodes()) {
                add(translatable.getString(), langTextsEnglish.get(translatable));
            }

            return;
        }

        if (locale.toLowerCase().startsWith("pt_")) {
            LanguagesTextsPortuguese langTextsPortuguese = new LanguagesTextsPortuguese();

            for (Component translatable : langTextsPortuguese.getTranslatableCodes()) {
                add(translatable.getString(), langTextsPortuguese.get(translatable));
            }
        }
    }
}