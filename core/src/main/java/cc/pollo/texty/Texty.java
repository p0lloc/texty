package cc.pollo.texty;

import cc.pollo.texty.component.TextyTemplateComponent;
import cc.pollo.texty.platform.Platform;
import cc.pollo.texty.source.LocalizationSource;
import cc.pollo.texty.text.Prefix;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

import static cc.pollo.texty.Constants.TEXTY_TRANSLATABLE_PREFIX;

public interface Texty {

    /**
     * Overload for {@link this#transform(Audience, Component, Prefix, boolean, Template...)} which uses MiniMessage <br>
     * and creates a translatable component from the key
     *
     * @param target target to transform this message for
     * @param key translatable key
     * @param prefix prefix to transform this message with
     * @param placeholders placeholders to replace
     * @return transformed component
     */
    default Component transform(Audience target, String key, Prefix prefix, Template... placeholders) {
        return transform(target, Component.translatable(key), prefix, true, placeholders);
    }

    default Component transform(Audience target, String key, Template... placeholders) {
        return transform(target, Component.translatable(key), true, placeholders);
    }

    default Component transform(Audience target, Component component) {
        return transform(target, component, false);
    }

    default Component transform(Audience target, Component component, @Nullable Prefix prefix){
        return transform(target, component, prefix, false);
    }

    default Component transform(Audience target, Component component, boolean useMiniMessage, Template... placeholders){
        return transform(target, component, null, useMiniMessage, placeholders);
    }

    default Component transform(Audience audience, Component component, @Nullable Prefix prefix,
                        boolean useMiniMessage, Template... placeholders){

        return transform(getPlatform().getLocale(audience), component, prefix, useMiniMessage, placeholders);
    }

    Component transform(Locale locale, Component component, @Nullable Prefix prefix,
                        boolean useMiniMessage, Template... placeholders);


    default Component renderComponent(Component component, Audience audience){
        return renderComponent(component, getPlatform().getLocale(audience));
    }

    default Component renderComponent(Component component, Locale locale) {
        return getCustomRenderer().render(component, locale);
    }

    void register();

    void unregister();

    Prefix getDefaultPrefix();

    Platform getPlatform();

    Chat getChat();

    void overrideDefaultRenderer() throws IllegalAccessException, ClassNotFoundException, NoSuchFieldException;

    CustomRenderer getCustomRenderer();

    TextyRenderer getTextyRenderer();

    static TranslatableComponent translatable(String key){
        return translatable(key, new Template[0]);
    }

    static TranslatableComponent translatable(String key, Component... args){
        return Component.translatable(TEXTY_TRANSLATABLE_PREFIX + key, args);
    }

    static TranslatableComponent translatable(String key, Template... placeholders){
        return Component.translatable(TEXTY_TRANSLATABLE_PREFIX + key,
                Arrays.stream(placeholders)
                        .map(TextyTemplateComponent::new)
                        .collect(Collectors.toList()));
    }

    interface Builder {

        Texty.Builder withLocalizationSources(LocalizationSource... localizationSources);

        Texty.Builder withMiniMessage(MiniMessage miniMessage);

        Texty.Builder withPlatform(Platform platform);

        Texty.Builder withDefaultPrefix(Prefix defaultPrefix);

        Texty.Builder overrideDefaultRenderer(boolean override);

        default Texty.Builder withDefaultPrefix(Component component){
            return withDefaultPrefix(Prefix.prefix(component));
        }

        default Texty.Builder withDefaultPrefix(String key){
            return withDefaultPrefix(Prefix.prefix(key));
        }

        Texty build();

    }

}