package net.klayil.veggycraft.neoforge.datagen;

import net.minecraft.network.chat.Component;

import java.util.List;

public class LanguagesTextsDetailed extends LanguagesTextsGeneral {
    final private boolean FINALIZED;
    @Override
    public void setTexts(String... keysAndValues) {
        super.master(keysAndValues);
    }

    @Override
    public String get(Component translatableCode) {
        String res = super.getMaster(translatableCode);

        assert FINALIZED;

        return res;
    }

    @Override
    public List<Component> getTranslatableCodes() {
        List<Component> res = super.getTranslatableCodesMaster();

        assert FINALIZED;
        return res;
    }

    public LanguagesTextsDetailed() {
        super();
        FINALIZED = true;

        //  log current instance of Class getName that extended LanguagesTextsDetailed
//          (probably 2 instances)
    }
}
