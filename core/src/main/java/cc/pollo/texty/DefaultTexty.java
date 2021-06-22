package cc.pollo.texty;

import cc.pollo.texty.platform.Platform;
import cc.pollo.texty.source.LocalizationSource;
import cc.pollo.texty.text.Prefix;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * Default Texty implementation
 */
public class DefaultTexty implements Texty {

    private final LocalizationSource[] sources;
    private final Platform platform;
    private final Prefix defaultPrefix;
    private final boolean overrideDefaultRenderer;
    private final CustomRenderer customRenderer;
    private final DefaultTextyRenderer textyRenderer;

    private final Chat chat;

    DefaultTexty(LocalizationSource[] sources,
                 MiniMessage miniMessage,
                 Platform platform,
                 Prefix defaultPrefix,
                 boolean overrideDefaultRenderer) {

        this.sources = sources;
        this.platform = platform;
        this.defaultPrefix = defaultPrefix;
        this.chat = new Chat(this);
        this.overrideDefaultRenderer = overrideDefaultRenderer;
        this.customRenderer = new CustomRenderer(GlobalTranslator.get(), miniMessage);
        this.textyRenderer = new DefaultTextyRenderer(customRenderer, platform);
    }

    @Override
    public Component transform(Locale locale, Component component, @Nullable Prefix prefix,
                               boolean useMiniMessage, Template... placeholders) {

        Component root = Component.text("");
        if (prefix != null) {

            Component prefixComponent = null;
            if (prefix instanceof Prefix.ComponentPrefix) {
                prefixComponent = ((Prefix.ComponentPrefix) prefix).getComponent();
            } else if (prefix instanceof Prefix.StringKeyPrefix) {
                prefixComponent = Component.translatable(((Prefix.StringKeyPrefix) prefix).getKey());
            }

            if (prefixComponent == null)
                return null;

            root = root.append(transform(locale, prefixComponent, null, (prefix instanceof Prefix.StringKeyPrefix), placeholders))
                       .append(Component.text(" "));
        }

        Component localized   = renderComponent(component, locale);
        Component transformed = useMiniMessage ? customRenderer.parseComponent(localized, placeholders) : localized;

        root = root.append(transformed);
        return root;
    }

    @Override
    public void register() {
        if(overrideDefaultRenderer) {
            try {
                overrideDefaultRenderer();
            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                return;
            }
        }

        platform.configureTextyRenderer(textyRenderer);

        GlobalTranslator globalTranslator = GlobalTranslator.get();
        for (LocalizationSource source : sources) {
            globalTranslator.addSource(source);
        }
    }

    @Override
    public void unregister() {
        GlobalTranslator globalTranslator = GlobalTranslator.get();
        for (LocalizationSource source : sources) {
            globalTranslator.removeSource(source);
        }
    }

    @Override
    public Prefix getDefaultPrefix() {
        return defaultPrefix;
    }

    @Override
    public Chat getChat() {
        return chat;
    }

    @Override
    public Platform getPlatform() {
        return platform;
    }

    @Override
    public void overrideDefaultRenderer() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class<?> translatorImpl = Class.forName("net.kyori.adventure.translation.GlobalTranslatorImpl");
        Field renderer = translatorImpl.getDeclaredField("renderer");
        renderer.setAccessible(true);
        GlobalTranslator globalTranslator = GlobalTranslator.get();
        renderer.set(globalTranslator, customRenderer);
    }

    @Override
    public CustomRenderer getCustomRenderer() {
        return customRenderer;
    }

    @Override
    public TextyRenderer getTextyRenderer() {
        return textyRenderer;
    }

    static class Builder implements Texty.Builder {

        private LocalizationSource[] localizationSource;
        private MiniMessage miniMessage = MiniMessage.get();
        private Prefix defaultPrefix = null;
        private boolean overrideDefaultRenderer = false;
        private Platform platform = null;

        @Override
        public Texty.Builder withLocalizationSources(LocalizationSource... localizationSource) {
            this.localizationSource = localizationSource;
            return this;
        }

        @Override
        public Texty.Builder withMiniMessage(MiniMessage miniMessage) {
            this.miniMessage = miniMessage;
            return this;
        }

        @Override
        public Texty.Builder withPlatform(Platform platform) {
            this.platform = platform;
            return this;
        }

        @Override
        public Texty.Builder withDefaultPrefix(Prefix defaultPrefix) {
            this.defaultPrefix = defaultPrefix;
            return this;
        }

        @Override
        public Texty.Builder overrideDefaultRenderer(boolean override) {
            this.overrideDefaultRenderer = override;
            return this;
        }

        @Override
        public Texty build() {
            return new DefaultTexty(localizationSource, miniMessage, platform, defaultPrefix, overrideDefaultRenderer);
        }

    }

    public static Texty.Builder builder(){
        return new DefaultTexty.Builder();
    }

}