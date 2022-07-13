package reminator.EdtBot.Commands.argument;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Arguments {
    Map<Argument<?>, Object> args = new HashMap<>();

    public <K> K get(Argument<K> argument) {
        Object o = args.get(argument);
        if (o == null) return (K) Optional.empty();
        return (K) o;
    }

    public <K> void add(Argument<?> arg, K value) {
        args.put(arg, value);
    }

    public <K> void add(Argument<K> arg, String value) {
        args.put(arg, value);
    }

    @Override
    public String toString() {
        return "Arguments{" +
                "args=" + args +
                '}';
    }
}
