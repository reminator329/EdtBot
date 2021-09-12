package reminator.EdtBot.Commands.genericEvent.guildEvent;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import reminator.EdtBot.Commands.genericEvent.action.CommandAction;
import reminator.EdtBot.Commands.genericEvent.action.LegacyCommandAction;

public class GuildUpdateEvent implements GuildEvent {

    private final GuildMessageUpdateEvent event;

    public GuildUpdateEvent(GuildMessageUpdateEvent event) {
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
