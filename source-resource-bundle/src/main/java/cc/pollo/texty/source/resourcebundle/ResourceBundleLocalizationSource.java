package cc.pollo.texty.source.resourcebundle;

import cc.pollo.texty.source.LocalizationSource;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.util.UTF8ResourceBundleControl;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Source loading from resource bundle (.properties file) through Adventure's own system.
 */
public class ResourceBundleLocalizationSource implements LocalizationSource {

    private final Key key;
    private final TranslationRegistry registry;
    private final String baseName;

    private final Class<?> clazz;
    private final boolean debug;

    ResourceBundleLocalizationSource(Key key, String baseName, Class<?> clazz, boolean debug){
        this.debug = debug;
        this.key      = key;
        this.registry = TranslationRegistry.create(key);
        this.baseName = baseName;
        this.clazz = clazz;
    }

    @Override
    public void load(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale,
                                                         clazz.getClassLoader(), new UTF8ResourceBundleControl());

        registry.registerAll(locale, bundle, false);
    }

    @Override
    @NotNull
    public @NonNull Key name() {
        return key;
    }

    @Override
    public @Nullable MessageFormat translate(@NonNull String key, @NonNull Locale locale) {
        debug("Translating " + key + " with " + locale.toLanguageTag());
        return registry.translate(key, locale);
    }

    public static class Builder {

        private Key key = Key.key("translations");
        private String baseName = "texty";
        private Class<?> loaderClass = getClass();
        private boolean debug = false;

        public Builder withKey(Key key){
            this.key = key;
            return this;
        }

        public Builder withBaseName(String baseName){
            this.baseName = baseName;
            return this;
        }

        /**
         * Specifies the class for requesting resources. <br>
         * This is needed since ResourceBundle might not work otherwise, simply <br>
         * calling <code>getClass()</code> should work most of the time.
         *
         * @param loaderClass class to load the bundle with
         * @return this builder
         */
        public Builder withClass(Class<?> loaderClass){
            this.loaderClass = loaderClass;
            return this;
        }

        public Builder withEnableDebug(boolean debug){
            this.debug = debug;
            return this;
        }

        public ResourceBundleLocalizationSource build(){
            return new ResourceBundleLocalizationSource(key, baseName, loaderClass, debug);
        }

    }

    public static Builder builder(){
        return new Builder();
    }

    private void debug(String message){
        if(debug)
            System.out.println(getClass().getSimpleName() + ": " + message);
    }

}