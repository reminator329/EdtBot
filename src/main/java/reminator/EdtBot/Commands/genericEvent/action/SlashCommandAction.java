package reminator.EdtBot.Commands.genericEvent.action;

import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
import org.jetbrains.annotations.NotNull;
import reminator.EdtBot.Commands.genericEvent.action.CommandAction;
import reminator.EdtBot.Commands.genericEvent.commandEvent.CommandEvent;

import java.util.function.Function;

public class SlashCommandAction implements CommandAction<InteractionHook> {

    private ReplyAction action;

    public SlashCommandAction(ReplyAction action) {
        this.action = action;
    }

    @Override
    public CommandAction<InteractionHook> setEphemeral(boolean ephemeral) {
        this.action = action.setEphemeral(ephemeral);
        return this;
    }

    @Override
    public <O> RestAction<O> flatMap(@NotNull Function<? super InteractionHook, ? extends RestAction<O>> flatMap) {
        return action.flatMap(flatMap);
    }

    @Override
    public void thenEdit(String message) {
        this.flatMap(v -> v.editOriginal(message));
    }
}
