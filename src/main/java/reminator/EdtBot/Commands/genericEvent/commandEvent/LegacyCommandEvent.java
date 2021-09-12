package reminator.EdtBot.Commands.genericEvent.commandEvent;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import reminator.EdtBot.Commands.genericEvent.action.CommandAction;
import reminator.EdtBot.Commands.genericEvent.guildEvent.GuildEvent;
import reminator.EdtBot.Commands.genericEvent.guildEvent.GuildReceivedEvent;
import reminator.EdtBot.Commands.genericEvent.guildEvent.GuildUpdateEvent;

public class LegacyCommandEvent implements CommandEvent {

    private final GuildEvent event;

    public LegacyCommandEvent(GenericGuildMessageEvent event) {
        if (event instanceof GuildMessageReceivedEvent) {
            this.event = new GuildReceivedEvent((GuildMessageReceivedEvent) event);
        } else if (event instanceof GuildMessageUpdateEvent) {
            this.event = new GuildUpdateEvent((GuildMessageUpdateEvent) event);
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
