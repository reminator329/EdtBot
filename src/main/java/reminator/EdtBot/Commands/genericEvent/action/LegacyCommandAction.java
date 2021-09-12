package reminator.EdtBot.Commands.genericEvent.action;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;
import reminator.EdtBot.Commands.genericEvent.action.CommandAction;
import reminator.EdtBot.Commands.genericEvent.commandEvent.CommandEvent;
import reminator.EdtBot.Commands.genericEvent.commandEvent.LegacyCommandEvent;

import java.util.function.Function;

public class LegacyCommandAction implements CommandAction<Message> {

    private final MessageAction action;

    public LegacyCommandAction(MessageAction action) {
        this.action = action;
    }

    @Override
    public CommandAction<Message> setEphemeral(boolean ephemeral) {
        return null;
    }

    @Override
    public <O> RestAction<O> flatMap(@NotNull Function<? super Message, ? extends RestAction<O>> flatMap) {
        return action.flatMap(flatMap);
    }

    @Override
    public void thenEdit(String message) {
        this.flatMap(m -> m.editMessage(message)).queue();
    }
}
