package cc.pollo.texty.platform;

import cc.pollo.texty.TextyRenderer;
import cc.pollo.texty.discovery.LocaleDiscovery;
import net.kyori.adventure.audience.Audience;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Locale;

public interface Platform {

    /**
     * Gets the locale for this Audience.
     *
     * @param target target to get locale ofr
     * @return identified locale or default if not applicable
     */
    @NonNull
    Locale getLocale(Audience target);

    /**
     * Gets the default locale for this platform.
     *
     * @return defualt locale
     */
    @NonNull Locale getDefaultLocale();

    /**
     * Method called to configure the texty renderer, such as registering new renderers
     * @param textyRenderer texty renderer to configure
     */
    default void configureTextyRenderer(TextyRenderer textyRenderer){ }

    /**
     * Builder for platforms.
     */
    interface Builder {

        /**
         * Specifies the default locale for this platform
         * @param defaultLocale default locale
         * @return this builder
         */
        Builder withDefaultLocale(Locale defaultLocale);

        /**
         * Specifies the locale discovery function for this platform
         * @param discovery locale discovery
         * @return this builder
         */
        Builder withDiscovery(LocaleDiscovery discovery);

        /**
         * Tells the platform if it should use client locale instead of custom discovery <br>
         * This always overrides {@link this#withDiscovery(LocaleDiscovery)}
         * @param clientLocale whether or not to use client locale
         * @return this builder
         */
        Builder useClientLocale(boolean clientLocale);

        /**
         * Builds the platform
         * @return instance of the platform
         */
        Platform build();

    }

}