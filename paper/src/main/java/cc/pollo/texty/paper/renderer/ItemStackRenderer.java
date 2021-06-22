package cc.pollo.texty.paper.renderer;

import cc.pollo.texty.render.Renderer;
import cc.pollo.texty.CustomRenderer;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Renders display name and lore on item stacks.
 */
public class ItemStackRenderer implements Renderer<ItemStack> {

    @Override
    public ItemStack render(ItemStack stack, CustomRenderer renderer, Locale locale) {
        ItemMeta itemMeta = stack.getItemMeta();
        if(itemMeta == null)
            return stack;

        Component displayName = itemMeta.displayName();
        if(displayName != null)
            itemMeta.displayName(renderer.render(displayName, locale));

        List<Component> lore = itemMeta.lore();
        if(lore != null)
            itemMeta.lore(lore.stream()
                    .filter(Objects::nonNull)
                    .map(c -> renderer.render(c, locale))
                    .collect(Collectors.toList()));

        stack.setItemMeta(itemMeta);
        return stack;
    }

}