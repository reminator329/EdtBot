package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import reminator.EdtBot.Categories.Category;
import reminator.EdtBot.Commands.argument.Argument;
import reminator.EdtBot.Commands.argument.Arguments;
import reminator.EdtBot.Commands.genericEvent.commandEvent.CommandEvent;
import reminator.EdtBot.bot.EdtBot;
import reminator.EdtBot.exceptions.ArgumentFormatException;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface Command {

    List<Argument<?>> getArguments();

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

    default String getShortDescription() {
        return getDescription();
    }

    default MessageEmbed.Field[] getExtraFields() {
        return new MessageEmbed.Field[0];
    }

    default String getSignature() {
        return getPrefix() + getLabel();
    }

    default Color getColor() {
        return EdtBot.color;
    }

    void execute(CommandEvent event, User author, MessageChannel channel, Arguments arguments);

    default boolean isAlias(String alias) {
        return Arrays.stream(getAlliass()).collect(Collectors.toList()).contains(alias);
    }

    default Optional<Arguments> getArguments(List<String> args) throws ArgumentFormatException {
        List<Argument<?>> argumentsMetadata = getArguments();
        long sizeMetadata = argumentsMetadata.stream().filter(a -> !a.isOptional()).count();
        int sizeArgs = args.size();

        if (sizeArgs < sizeMetadata) return Optional.empty();

        Arguments arguments = new Arguments();

        for (int i = 0; i < sizeArgs; i++) {
            Argument<?> metadata = argumentsMetadata.get(i);
            String arg = args.get(i);

            try {
                arguments.add(metadata, metadata.get(arg));
            } catch (NumberFormatException e) {
                throw new ArgumentFormatException(metadata, arg);
            }
        }
        return Optional.of(arguments);
    }
}
