package net.klayil.veggycraft.neoforge.datagen;

import net.minecraft.network.chat.Component;

import java.util.List;

abstract public class LanguagesTextsGeneral extends LanguagesTextsGeneralMaster {
    public abstract void setTexts(String... keysAndValues);
    public abstract String get(Component translatableCode);
    public abstract List<Component> getTranslatableCodes();

    public LanguagesTextsGeneral(String... keysAndValues) {
        super(true);
        this.setTexts(keysAndValues);
    }
}
