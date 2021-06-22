package cc.pollo.texty.text;

import net.kyori.adventure.text.Component;

public interface Prefix {

    static Prefix prefix(String stringKey){
        return new StringKeyPrefix(stringKey);
    }

    static Prefix prefix(Component component){
        return new ComponentPrefix(component);
    }

    class StringKeyPrefix implements Prefix {

        private final String key;

        public StringKeyPrefix(String key){
            this.key = key;
        }

        public String getKey() {
            return key;
        }

    }

    class ComponentPrefix implements Prefix {

        private final Component component;

        public ComponentPrefix(Component component) {
            this.component = component;
        }

        public Component getComponent() {
            return component;
        }
    }

}