package cc.pollo.texty.discovery;

import net.kyori.adventure.identity.Identified;

import java.util.Locale;
import java.util.function.BiFunction;

public interface LocaleDiscovery extends BiFunction<Locale, Identified, Locale> { }