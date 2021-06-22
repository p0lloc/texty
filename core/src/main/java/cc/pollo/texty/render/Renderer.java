package cc.pollo.texty.render;

import cc.pollo.texty.CustomRenderer;

import java.util.Locale;

public interface Renderer<T> {

    T render(T t, CustomRenderer renderer, Locale locale);

}