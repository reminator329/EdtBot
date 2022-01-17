package reminator.EdtBot.Commands.genericEvent.commandEvent;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import reminator.EdtBot.Commands.genericEvent.action.CommandAction;
import reminator.EdtBot.Commands.genericEvent.guildEvent.GuildEvent;
import reminator.EdtBot.Commands.genericEvent.guildEvent.GuildReceivedEvent;
import reminator.EdtBot.Commands.genericEvent.guildEvent.GuildUpdateEvent;

public class LegacyCommandEvent implements CommandEvent {

    private final GuildEvent event;

    public LegacyCommandEvent(GenericMessageEvent event) {
        if (event instanceof MessageReceivedEvent) {
            this.event = new GuildReceivedEvent((MessageReceivedEvent) event);
        } else if (event instanceof MessageUpdateEvent) {
            this.event = new GuildUpdateEvent((MessageUpdateEvent) event);
        } else {
            this.event = null;
        }
    }

    public GuildEvent getEvent() {
        return this.event;
    }

    public Member getMember() {
        if (event == null) return null;
        return event.getMember();
    }

    @Override
    public Guild getGuild() {
        if (event == null) return null;
        return event.getChannel().getGuild();
    }

    @Override
    public CommandAction<Message> reply(String message) {
        if (event == null) return null;
        return event.sendMessage(message);
    }
}
