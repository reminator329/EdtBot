package reminator.EdtBot.Commands.genericEvent.guildEvent;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import reminator.EdtBot.Commands.genericEvent.action.CommandAction;

public interface GuildEvent {
    Member getMember();
    CommandAction<Message> sendMessage(String message);
    CommandAction<Message> sendMessage(MessageEmbed messageEmbed);
    TextChannel getChannel();
}
