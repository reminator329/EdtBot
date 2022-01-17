package reminator.EdtBot.bot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import reminator.EdtBot.Commands.Command;
import reminator.EdtBot.Commands.argument.Arguments;
import reminator.EdtBot.Commands.enums.Commands;
import reminator.EdtBot.Commands.genericEvent.commandEvent.LegacyCommandEvent;
import reminator.EdtBot.exceptions.ArgumentFormatException;

import java.time.Duration;
import java.util.*;

public class Controller extends ListenerAdapter {

    public Controller() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {

        for (Commands c : Commands.values()) {
            Command cmd = c.getCommand();
            String prefix = cmd.getPrefix();
            String label = cmd.getLabel();
            String prefixLabel = prefix + label;

            if (event.getName().equals(label)) {
                Optional<Arguments> arguments = Optional.empty();
                try {
                    arguments = cmd.getArguments(event.getOptions().stream().map(OptionMapping::getAsString).toList());
                } catch (ArgumentFormatException e) {
                    event.getChannel().sendMessage(e.getMessage()).queue();
                }

                if (arguments.isPresent()) {
                    cmd.execute(new reminator.EdtBot.Commands.genericEvent.commandEvent.SlashCommandEvent(event), event.getUser(), event.getChannel(), arguments.get());
                } else {
                    event.getChannel().sendMessage("Commande mal utilisée, voir `" + prefix + "help " + label + "`.").queue();
                }
                return;
            }
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (event.isFromGuild()) {
            commandSent(event.getAuthor(), event.getMessage(), event);
        } else {
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

    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {
        if (!event.isFromGuild()) return;
        commandSent(event.getAuthor(), event.getMessage(), event);
    }

    private void commandSent(User author, Message message, @NotNull GenericMessageEvent event) {
        if (author.isBot()) return;
        List<String> args = new ArrayList<>(Arrays.asList(message.getContentRaw().split("\\s+")));
        String command = args.get(0);
        args.remove(0);
        verifyBeforeExecute(command, args, author, event);
    }

    private void verifyBeforeExecute(String command, List<String> args, User author, @NotNull GenericMessageEvent event) {

        for (Commands c : Commands.values()) {
            Command cmd = c.getCommand();
            String prefix = cmd.getPrefix();
            String label = cmd.getLabel();
            String prefixLabel = prefix + label;

            String[] separation = command.split("(?i)" + prefix);

            if (prefixLabel.equalsIgnoreCase(command) || separation.length > 1 && cmd.isAlias(separation[1])) {

                Optional<Arguments> arguments = Optional.empty();
                try {
                    arguments = cmd.getArguments(args);
                } catch (ArgumentFormatException e) {
                    event.getChannel().sendMessage(e.getMessage()).queue();
                }

                if (arguments.isPresent()) {
                    cmd.execute(new LegacyCommandEvent(event), author, event.getChannel(), arguments.get());
                } else {
                    event.getChannel().sendMessage("Commande mal utilisée, voir `" + prefix + "help " + label + "`.").queue();
                }
                return;
            }
        }
    }
}