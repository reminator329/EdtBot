package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import reminator.EdtBot.bot.EdtBot;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public interface Command {

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

    void execute(GuildMessageReceivedEvent event);

    default boolean isAlias(String alias) {
        return Arrays.stream(getAlliass()).collect(Collectors.toList()).contains(alias);
    }
}
