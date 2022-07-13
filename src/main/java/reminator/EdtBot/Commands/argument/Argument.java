package reminator.EdtBot.Commands.argument;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class Argument<K> {

    protected boolean optional = false;

    private static final Map<Class<?>, Function<String, ?>> converters = Map.ofEntries(
            Map.entry(Integer.class, Integer::parseInt),
            Map.entry(Boolean.class, string -> string.matches("(?i)vrai|oui|true|yes")),
            Map.entry(String.class, String::toString)
    );
    private final Class<K> type;
    private final String name;
    private final String description;

    public Argument(Class<K> type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public K get(String s) {
        return (K) converters.get(type).apply(s);
    }

    protected <T> T get(Class<T> type, String s) {
        if (s == null) return null;
        return (T) converters.get(type).apply(s);
    }

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isOptional() {
        return optional;
    }

    @Override
    public String toString() {
        return "Argument{" +
                "type=" + type +
                ", name='" + name + '\'' +
                '}';
    }
}
