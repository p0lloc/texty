package cc.pollo.texty;

import cc.pollo.texty.platform.Platform;
import cc.pollo.texty.render.Renderer;
import net.kyori.adventure.audience.Audience;

public interface TextyRenderer {

    static TextyRenderer renderer(CustomRenderer customRenderer, Platform platform){
        return new DefaultTextyRenderer(customRenderer, platform);
    }

    <T> T render(T t, Class<T> tClass, Audience audience);

    <T> void registerRenderer(Class<T> tClass, Renderer<T> renderer);

}