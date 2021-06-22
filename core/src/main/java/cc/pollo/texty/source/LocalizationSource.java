package cc.pollo.texty.source;

import net.kyori.adventure.translation.Translator;

import java.util.Locale;

public interface LocalizationSource extends Translator {

    void load(Locale locale);

    default void loadAll(Locale... locales){
        for (Locale locale : locales) {
            load(locale);
        }
    }

}