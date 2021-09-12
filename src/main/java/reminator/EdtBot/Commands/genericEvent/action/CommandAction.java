package reminator.EdtBot.Commands.genericEvent.action;

import net.dv8tion.jda.api.requests.RestAction;
import reminator.EdtBot.Commands.genericEvent.commandEvent.CommandEvent;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Function;

public interface CommandAction<T> {
    CommandAction<T> setEphemeral(boolean ephemeral);

    <O> RestAction<O> flatMap(@Nonnull Function<? super T, ? extends RestAction<O>> flatMap);

    void thenEdit(String message);
}
