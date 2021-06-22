package cc.pollo.texty;

import cc.pollo.texty.text.Prefix;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Template;
import org.jetbrains.annotations.Nullable;

/**
 * Chat module, provides easy-to-use methods for sending messages through Texty
 */
public final class Chat {

    private final Texty texty;

    public Chat(Texty texty){
        this.texty = texty;
    }

    public void sendWithDefaultPrefix(Audience target, String key, Template... placeholders) {
        target.sendMessage(texty.transform(target, key, texty.getDefaultPrefix(), placeholders));
    }
    
    public void sendWithDefaultPrefix(Audience target, Component component) {
        target.sendMessage(texty.transform(target, component, texty.getDefaultPrefix()));
    }

    public void send(Audience target, String key, Prefix prefix, Template... placeholders) {
        target.sendMessage(texty.transform(target, key, prefix, placeholders));
    }

    public void send(Audience target, String key, Template... placeholders) {
        target.sendMessage(texty.transform(target, key, placeholders));
    }

    public void send(Audience target, Component component) {
        target.sendMessage(texty.transform(target, component));
    }

    public void send(Audience target, Component component, @Nullable Prefix prefix){
        target.sendMessage(texty.transform(target, component, prefix));
    }

    public void send(Audience target, Component component, boolean useMiniMessage, Template... placeholders){
        target.sendMessage(texty.transform(target, component, useMiniMessage, placeholders));
    }

    public void send(Audience target, Component component, @Nullable Prefix prefix, boolean useMiniMessage, Template... placeholders){
        target.sendMessage(texty.transform(target, component, prefix, useMiniMessage, placeholders));
    }

}