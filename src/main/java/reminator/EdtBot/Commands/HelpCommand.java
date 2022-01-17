package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import reminator.EdtBot.Categories.Category;
import reminator.EdtBot.Categories.enums.Categories;
import reminator.EdtBot.Commands.argument.Argument;
import reminator.EdtBot.Commands.argument.Arguments;
import reminator.EdtBot.Commands.argument.OptionalArgument;
import reminator.EdtBot.Commands.enums.Commands;
import reminator.EdtBot.Commands.genericEvent.commandEvent.CommandEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HelpCommand implements Command {
    private final Argument<Optional<String>> command = new OptionalArgument<>(String.class, "command", "Affiche la page d'aide de cette commande, si elle existe.");

    @Override
    public List<Argument<?>> getArguments() {
        return List.of(command);
    }

    @Override
    public Category getCategory() {
        return Categories.OTHER.getCategory();
    }

    @Override
    public String getLabel() {
        return "help";
    }

    @Override
    public String[] getAlliass() {
        return new String[]{"h", "aide", "a", "commandes", "commands", "c"};
    }

    @Override
    public String getDescription() {
        return "Permet de savoir comment utiliser les commandes.";
    }

    @Override
    public String getSignature() {
        return Command.super.getSignature() + " [commande]";
    }

    @Override
    public void execute(CommandEvent event, User author, MessageChannel channel, Arguments arguments) {

        EmbedBuilder message;

        Optional<String> optCommand = arguments.get(command);
        String nameCommand = optCommand.orElse("");

        if (optCommand.isEmpty()) {
            message = this.help();
        } else {
            Command command = Commands.getCommand(optCommand.get());
            message = this.help(command);
        }

        if (message == null) {
            channel.sendMessage("La commande `" + nameCommand + "` n'existe pas").queue();
            return;
        }

        if (author != null)
            message.setFooter(author.getName(), author.getAvatarUrl());
        channel.sendMessageEmbeds(message.build()).queue();
    }

    private EmbedBuilder help() {

        EmbedBuilder builder = new EmbedBuilder();
        Map<Category, List<Command>> commandsGroupedByCategory = Commands.getCommandsGroupedByCategory();

        final String titre = "Liste des commandes de l'EdtBot";
        final String imageI = "https://image.flaticon.com/icons/png/512/1301/1301429.png";

        builder.setThumbnail(imageI);
        builder.setColor(getColor());
        builder.setTitle(titre, "https://www.remontees-mecaniques.net/");
        builder.appendDescription(getDescription());

        for (Map.Entry<Category, List<Command>> categoryListEntry : commandsGroupedByCategory.entrySet()) {
            Category category = categoryListEntry.getKey();
            List<Command> commands = categoryListEntry.getValue();

            String titreField = category.getName();
            String descriptionField = category.getDescription() + "\n" + commands.stream().map(cmd -> String.format("`%s`", cmd.getLabel())).collect(Collectors.joining(" "));
            builder.addField(titreField, descriptionField, false);
        }
        return builder;
    }

    private EmbedBuilder help(Command command) {

        if (command == null) {
            return null;
        }

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(command.getColor());
        builder.setTitle("Commande " + command.getName());
        builder.appendDescription(command.getDescription());

        builder.addField("Signature", "`" + command.getSignature() + "`", false);

        String[] alliass = command.getAlliass();
        if (alliass.length != 0) {
            builder.addField("Alias", Arrays.stream(alliass).map(al -> String.format("`%s`", al)).collect(Collectors.joining(" ")), false);
        }

        MessageEmbed.Field[] extras = command.getExtraFields();
        if (extras.length != 0) {
            Arrays.stream(extras).forEach(builder::addField);
        }

        return builder;
    }
}
