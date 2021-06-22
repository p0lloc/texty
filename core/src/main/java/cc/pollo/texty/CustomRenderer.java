package cc.pollo.texty;

import cc.pollo.texty.util.ComponentTemplateTransformer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.text.renderer.TranslatableComponentRenderer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.kyori.adventure.translation.Translator;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Locale;

public class CustomRenderer extends TranslatableComponentRenderer<Locale> {

    private final Translator source;
    private final MiniMessage miniMessage;

    public CustomRenderer(Translator source, MiniMessage miniMessage){
        this.source      = source;
        this.miniMessage = miniMessage;
    }

    @Override
    @NotNull
    public @NonNull Component render(@NonNull Component component, @NonNull Locale context) {
        Component render = super.render(component, context);

        if(component instanceof TranslatableComponent){
            TranslatableComponent translatable = (TranslatableComponent) component;
            String key = translatable.key();

            if(key.startsWith(Constants.TEXTY_TRANSLATABLE_PREFIX)) {
                render = parseComponent(render,
                        ComponentTemplateTransformer.constructTemplates(translatable.args()));
            }
        }

        return render;
    }

    @Override
    protected @Nullable MessageFormat translate(@NonNull String key, @NonNull Locale context) {
        if(key.startsWith(Constants.TEXTY_TRANSLATABLE_PREFIX))
            key = key.replace(Constants.TEXTY_TRANSLATABLE_PREFIX, ""); // Remove the prefix

        return source.translate(key, context);
    }

    public Component parseComponent(Component component, Template... placeholders){
        // TODO wait for paper to update
        return miniMessage.parse(PlainComponentSerializer.plain().serialize(component), placeholders);
    }

}