package cc.pollo.texty.source.bukkityaml;

import cc.pollo.texty.source.LocalizationSource;
import net.kyori.adventure.key.Key;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Source loading from YAML files through Bukkit configuration system
 */
public class BukkitYAMLLocalizationSource implements LocalizationSource {

    private final Key name;
    private final File folder;
    private final boolean debug;
    private final String fileNameFormat;

    private final Map<Locale, ConfigurationSection> localizationMap = new HashMap<>();

    BukkitYAMLLocalizationSource(Key name, File folder,
                                 boolean debug, String fileNameFormat) {

        this.name = name;
        this.folder = folder;
        this.debug = debug;
        this.fileNameFormat = fileNameFormat;
    }

    @Override
    public @Nullable MessageFormat translate(@NonNull String key, @NonNull Locale locale) {
        debug("Translating " + key + " with " + locale.toLanguageTag());

        ConfigurationSection section = localizationMap.get(locale);
        if(section == null) {
            debug("Could not find specific locale");
            section = localizationMap.get(new Locale(locale.getLanguage()));
            if(section == null) {
                debug("Could not find by language");
                section = localizationMap.get(Locale.US);
                if(section == null)
                    return null;
            }
        }

        String format = null;
        if(section.isString(key)){
            format = section.getString(key);
        } else if(section.isList(key)){
            List<String> stringList = section.getStringList(key);
            String newValue = String.join("\n", stringList);
            section.set(key, newValue); // Set it in memory so we don't need to transform it every time
            format = newValue;
        }

        if(format == null)
            return null;

        debug("Successful translation");
        return new MessageFormat(format, locale);
    }

    @Override
    @NotNull
    public @NonNull Key name() {
        return name;
    }

    @Override
    public void load(Locale locale) {
        String fileName = String.format(fileNameFormat, locale.toLanguageTag().replace("-", "_"));
        File file = new File(folder, fileName);

        debug("Loading " + locale.toLanguageTag() + " from " + file.getPath());
        FileConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        localizationMap.put(locale, config);
    }

    public static class Builder {

        private Key key = Key.key("translations");
        private File folder = new File(".");
        private boolean debug = false;
        private String fileNameFormat = "lang_%s.yml";

        private Builder(){}

        public Builder withKey(Key key){
            this.key = key;
            return this;
        }

        public Builder withFolder(File folder){
            this.folder = folder;
            return this;
        }

        public Builder withEnableDebug(boolean debug){
            this.debug = debug;
            return this;
        }

        public Builder withFileNameFormat(String fileNameFormat){
            this.fileNameFormat = fileNameFormat;
            return this;
        }

        public BukkitYAMLLocalizationSource build(){
            return new BukkitYAMLLocalizationSource(key, folder, debug, fileNameFormat);
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