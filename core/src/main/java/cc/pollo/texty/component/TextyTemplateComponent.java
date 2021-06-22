package cc.pollo.texty.component;

import cc.pollo.texty.Constants;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a component which has the value of a template. <br>
 * This is used to store templates as components which will later be transformed into templates again.
 */
public class TextyTemplateComponent implements ComponentLike {

    private final Template template;

    public TextyTemplateComponent(Template template){
        this.template = template;
    }

    public Template getTemplate() {
        return template;
    }

    @Override
    @NotNull
    public @NonNull Component asComponent() {
        if(template instanceof Template.ComponentTemplate){
            return ((Template.ComponentTemplate) template).value();
        } else if(template instanceof Template.StringTemplate){
            Template.StringTemplate stringTemplate = (Template.StringTemplate) this.template;
            return Component.text(stringTemplate.key() +
                    Constants.TEXTY_TEMPLATE_DELIMITER + stringTemplate.value());
        }

        return Component.text("");
    }

}