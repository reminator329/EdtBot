package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import reminator.EdtBot.Categories.Category;
import reminator.EdtBot.bot.EdtBot;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface Command {

    default String getName() {
        return getLabel().replace('-', ' ');
    }

    Category getCategory();

    default String getPrefix() {
        return EdtBot.prefix;
    }

    String getLabel();

    String[] getAlliass();

    String getDescription();

    default String getSignature() {
        return getPrefix() + getLabel();
    }

    default Color getColor() {
        return EdtBot.color;
    }

    void execute(GuildMessageReceivedEvent event, User author, MessageChannel channel, List<String> args);

    default boolean isAlias(String alias) {
        return Arrays.stream(getAlliass()).collect(Collectors.toList()).contains(alias);
    }
}
