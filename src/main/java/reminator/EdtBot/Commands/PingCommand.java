package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import reminator.EdtBot.Categories.Category;
import reminator.EdtBot.Categories.enums.Categories;
import reminator.EdtBot.bot.BotEmbed;

import java.util.List;

public class PingCommand implements Command {

    @Override
    public Category getCategory() {
        return Categories.OTHER.getCategory();
    }

    @Override
    public String getLabel() {
        return "ping";
    }

    @Override
    public String[] getAlliass() {
        return new String[]{"p"};
    }

    @Override
    public String getDescription() {
        return "Répond pong et affiche la musique écoutée actuellement sur spotify.";
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, User author, MessageChannel channel, List<String> args) {

        EmbedBuilder builder = BotEmbed.SPOTIFY.getBuilder(event.getMember());
        builder.setTitle("Pong !");

        channel.sendMessage(builder.build()).queue();
    }
}
