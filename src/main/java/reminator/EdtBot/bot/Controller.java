package reminator.EdtBot.bot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import reminator.EdtBot.Categories.OtherCategory;
import reminator.EdtBot.Categories.Category;
import reminator.EdtBot.Categories.EdtCategory;
import reminator.EdtBot.Commands.*;
import reminator.EdtBot.Commands.enums.Commands;

import java.time.Duration;
import java.util.ArrayList;
import java.util.TimeZone;

public class Controller extends ListenerAdapter {

    public Controller() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.isFromGuild()) return;

        event.getAuthor().openPrivateChannel()
                .flatMap(channel -> channel.sendMessage("3"))
                .delay(Duration.ofSeconds(1))
                .flatMap(channel -> channel.editMessage("2"))
                .delay(Duration.ofSeconds(1))
                .flatMap(channel -> channel.editMessage("1"))
                .delay(Duration.ofSeconds(1))
                .flatMap(Message::delete)
                .queue();
    }

    @Override
    public void onGuildMessageUpdate(@NotNull GuildMessageUpdateEvent event) {
        System.out.println("test");
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        for (Commands c : Commands.values()) {
            String prefixLabel = c.getCommand().getPrefix() + c.getCommand().getLabel();
            String[] test = event.getMessage().getContentRaw().split(c.getCommand().getPrefix());
            if (prefixLabel.equalsIgnoreCase(args[0]) || test.length > 1 && c.getCommand().isAlias(test[1])) {
                c.getCommand().execute(event);
            }
        }
    }
}