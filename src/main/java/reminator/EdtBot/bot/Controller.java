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
import reminator.EdtBot.utils.EcouteursEdt;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        //event.getChannel().sendMessage("!p test").queue();
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        List<String> args = new ArrayList<>(Arrays.asList(event.getMessage().getContentRaw().split("\\s+")));

        String command = args.get(0);

        for (Commands c : Commands.values()) {
            Command cmd = c.getCommand();
            String prefix = cmd.getPrefix();
            String label = cmd.getLabel();
            String prefixLabel = prefix + label;

            String[] separation = command.split(prefix);

            if (prefixLabel.equalsIgnoreCase(command) || separation.length > 1 && cmd.isAlias(separation[1])) {
                args.remove(0);
                c.getCommand().execute(event, event.getAuthor(), event.getChannel(), args);
                return;
            }
        }
    }
}