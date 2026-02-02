package net.klayil.veggycraft.neoforge.datagen;

import net.klayil.veggycraft.VeggyCraft;
import net.minecraft.network.chat.Component;

import java.util.*;
import java.util.function.Consumer;

public class LanguagesTextsGeneralMaster {
    protected String getMaster(Component translatableCode) {
        String key = translatableCode.getString();

        if (texts.containsKey(key)) return texts.get(key);
        throw new NoSuchElementException("No result for translatable code `%s` found!".formatted(key));
    }

    public static String classNameFinalized;


    protected List<Component> getTranslatableCodesMaster() {
        return new ArrayList<>(getListFromMap());
    }

    private static List<Component> getListFromMap() {
        return LanguagesTextsGeneralMaster.texts.keySet().stream().map(
                (str) -> { return ( (Component) Component.translatable(str) ); }
        ).toList();
    }

    protected static HashMap<String, String> create(String[] keysAndValues) {
        assert keysAndValues.length % 2 == 0;
        HashMap<String, String> res = new HashMap<>();

        fill(res, keysAndValues, 1);

        return res;
    }

    private static void fill(HashMap<String, String> map, String[] data, int last_ableIndex) {
        if (last_ableIndex >= data.length) {
            return;
        }

        Consumer<Integer> lamb = (Integer ind) -> {
            String key = data[ind-1];
            String val = data[ind];

            VeggyCraft.LOGGER.warn("^IN <%s> key: [%s] // value: ´%s´", classNameFinalized, key, val);

            map.put(key, val);
        };
        lamb.accept(last_ableIndex);

        fill(map, data, last_ableIndex+2);
    }

    public static HashMap<String, String> texts;

    private void setTextsMaster(String... keysAndValues) {
        texts = create(keysAndValues);
    }

    public void setTexts(String... keysAndValues) {
        this.setTextsMaster(keysAndValues);
    };

    void master(String... keysAndValues) {
        this.setTextsMaster(keysAndValues);
    }

    final LanguagesTextsGeneralMaster self;
    public LanguagesTextsGeneralMaster(String... keysAndValues) {
        this.setTexts(keysAndValues);

        self = this;

        classNameFinalized = this.getClass().getSimpleName();
    }

    public LanguagesTextsGeneralMaster(boolean unset) {
        self = this;

        classNameFinalized = this.getClass().getSimpleName();
    }
}
