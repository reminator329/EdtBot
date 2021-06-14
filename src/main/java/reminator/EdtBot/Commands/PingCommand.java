package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
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
    public void execute(GenericGuildMessageEvent e, User author, MessageChannel channel, List<String> args) {

        EmbedBuilder builder;
        if (e instanceof GuildMessageReceivedEvent event) {
            builder = BotEmbed.SPOTIFY.getBuilder(event.getMember());
        } else if (e instanceof GuildMessageUpdateEvent event){
            builder = BotEmbed.SPOTIFY.getBuilder(event.getMember());
        } else {
            return;
        }

        builder.setTitle("Pong !");

        channel.sendMessage(builder.build()).queue();
    }
}
