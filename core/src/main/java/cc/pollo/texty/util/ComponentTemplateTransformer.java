package cc.pollo.texty.util;

import cc.pollo.texty.Constants;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.Template;

import java.util.Collection;

public final class ComponentTemplateTransformer {

    private ComponentTemplateTransformer(){}

    /**
     * Constructs MiniMessage templates from componentlikes <br>
     * This especially uses the content provided by {@link cc.pollo.texty.component.TextyTemplateComponent}
     *
     * @param components collection of component likes
     * @return array of templates
     */
    public static Template[] constructTemplates(Collection<? extends ComponentLike> components){
        int i = 0;
        Template[] templates = new Template[components.size()];
        for (ComponentLike componentLike : components) {
            Template template = null;
            if(componentLike instanceof TextComponent) {
                TextComponent textComponent = (TextComponent) componentLike;
                String content = textComponent.content();
                String[] split = content.split(Constants.TEXTY_TEMPLATE_DELIMITER);

                if (split.length == 2)
                    template = Template.of(split[0], split[1]);
            }

            if(template == null) // No custom texty template found, use pure as-is
                template = Template.of(String.valueOf(i), componentLike.asComponent());

            templates[i] = template;
            i++;
        }

        return templates;
    }

}