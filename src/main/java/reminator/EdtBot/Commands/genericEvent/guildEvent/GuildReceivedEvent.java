package reminator.EdtBot.Commands.genericEvent.guildEvent;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import reminator.EdtBot.Commands.genericEvent.action.CommandAction;
import reminator.EdtBot.Commands.genericEvent.action.LegacyCommandAction;

public class GuildReceivedEvent implements GuildEvent {

    private final GuildMessageReceivedEvent event;

    public GuildReceivedEvent(GuildMessageReceivedEvent event) {
        this.event = event;
    }

    @Override
    public Member getMember() {
        return event.getMember();
    }

    @Override
    public CommandAction<Message> sendMessage(String message) {
        return new LegacyCommandAction(event.getChannel().sendMessage(message));
    }

    @Override
    public CommandAction<Message> sendMessage(MessageEmbed messageEmbed) {
        return new LegacyCommandAction(event.getChannel().sendMessage(messageEmbed));
    }

    @Override
    public TextChannel getChannel() {
        return event.getChannel();
    }
}
