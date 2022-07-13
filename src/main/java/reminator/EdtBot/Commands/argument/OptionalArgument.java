package reminator.EdtBot.Commands.argument;

import reminator.EdtBot.utils.TypeToken;

import java.util.Optional;

public class OptionalArgument<K> extends Argument<Optional<K>> {

    private Class<K> type;

    public OptionalArgument(Class<K> type, String name, String description) {
        super(new TypeToken<Optional<K>>(){}.getType(), name, description);
        this.type = type;
        this.optional = true;
    }

    @Override
    public Optional<K> get(String s) {
        return Optional.ofNullable(super.get(type, s));
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}
