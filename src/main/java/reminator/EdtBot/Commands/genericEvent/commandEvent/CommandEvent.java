package reminator.EdtBot.Commands.genericEvent.commandEvent;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import reminator.EdtBot.Commands.genericEvent.action.CommandAction;

public interface CommandEvent {
    CommandAction<?> reply(String message);
    Member getMember();
    Guild getGuild();
}
