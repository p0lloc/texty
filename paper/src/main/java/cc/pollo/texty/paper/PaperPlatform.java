package cc.pollo.texty.paper;

import cc.pollo.texty.TextyRenderer;
import cc.pollo.texty.discovery.LocaleDiscovery;
import cc.pollo.texty.paper.renderer.ItemStackRenderer;
import cc.pollo.texty.platform.Platform;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identified;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Locale;

/**
 * Platform implementation for Paper servers.
 */
public class PaperPlatform implements Platform {

    private final LocaleDiscovery localeDiscovery;
    private final Locale defaultLocale;

    private static final LocaleDiscovery CLIENT_LOCALE_DISCOVERY = (defaultLocale, identified) -> {
        if(identified instanceof Player){
            return ((Player) identified).locale();
        } else {
            return defaultLocale;
        }
    };

    private PaperPlatform(LocaleDiscovery localeDiscovery, @NonNull Locale defaultLocale){
        this.localeDiscovery = localeDiscovery;
        this.defaultLocale   = defaultLocale;
    }

    @Override
    @NonNull
    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    @Override
    public @NonNull Locale getLocale(Audience target) {
        if(target instanceof Identified){
            return localeDiscovery.apply(defaultLocale, (Identified) target);
        } else {
            return defaultLocale;
        }
    }

    @Override
    public void configureTextyRenderer(TextyRenderer textyRenderer) {
        textyRenderer.registerRenderer(ItemStack.class, new ItemStackRenderer());
    }

    static class Builder implements Platform.Builder {

        private Locale defaultLocale = Locale.ENGLISH;
        private LocaleDiscovery discovery = (defaultLocale, i) -> defaultLocale;
        private boolean useClientLocale = false;

        @Override
        public Platform.Builder withDefaultLocale(Locale defaultLocale) {
            this.defaultLocale = defaultLocale;
            return this;
        }

        @Override
        public Platform.Builder useClientLocale(boolean clientLocale) {
            this.useClientLocale = clientLocale;
            return this;
        }

        @Override
        public Platform.Builder withDiscovery(LocaleDiscovery discovery) {
            this.discovery = discovery;
            return this;
        }

        @Override
        public Platform build() {
            return new PaperPlatform(useClientLocale ? CLIENT_LOCALE_DISCOVERY : discovery, defaultLocale);
        }

    }

    /**
     * Returns a new instance of the Paper platform builder
     * @return builder
     */
    public static Platform.Builder builder(){
        return new Builder();
    }

}