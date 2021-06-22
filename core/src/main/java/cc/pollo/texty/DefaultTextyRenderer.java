package cc.pollo.texty;

import cc.pollo.texty.platform.Platform;
import cc.pollo.texty.render.Renderer;
import net.kyori.adventure.audience.Audience;

import java.util.HashMap;
import java.util.Map;

public class DefaultTextyRenderer implements TextyRenderer {

    private final Map<Class<?>, Renderer<?>> renderers = new HashMap<>();
    private final CustomRenderer renderer;
    private final Platform platform;

    DefaultTextyRenderer(CustomRenderer renderer, Platform platform){
        this.renderer = renderer;
        this.platform = platform;
    }

    @SuppressWarnings("unchecked")
    public <T> T render(T t, Class<T> tClass, Audience audience){
        if (renderers.containsKey(tClass)) {
            Renderer<T> renderer = (Renderer<T>) renderers.get(tClass);
            return renderer.render(t, this.renderer, platform.getLocale(audience));
        }

        return t;
    }

    public <T> void registerRenderer(Class<T> tClass, Renderer<T> renderer){
        renderers.put(tClass, renderer);
    }

}