package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import reminator.EdtBot.Categories.Category;
import reminator.EdtBot.Categories.enums.Categories;
import reminator.EdtBot.Commands.enums.Commands;
import reminator.EdtBot.bot.EdtBot;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HelpCommand implements Command {

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

    private MessageEmbed help() {

        EmbedBuilder builder = new EmbedBuilder();
        Categories[] categories = Categories.values();

        final String titre = "Liste des commandes de l'EdtBot";
        final String imageI = "https://image.flaticon.com/icons/png/512/1301/1301429.png";

        builder.setThumbnail(imageI);
        builder.setColor(Color.RED);
        builder.setTitle(titre, "https://www.remontees-mecaniques.net/");
        builder.appendDescription("Utilise `r!help <commande>` pour plus d'informations sur une commande.");
        for (Categories cat : categories) {
            String titreField = cat.getCategory().getName();
            StringBuilder descriptionField = new StringBuilder(cat.getCategory().getDescription() + "\n");

            // TODO à changer le for
            /*
                for (Command c : cat.getCategory().getCommands()) {
                descriptionField.append("`").append(c.getLabel()).append("` ");
            }*/
            builder.addField(titreField, descriptionField.toString(), false);
        }
        return builder.build();
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, User author, MessageChannel channel, List<String> args) {

        MessageEmbed message = null;

        if (args.size() == 0) {
            if (author != null) {
                EmbedBuilder preMessage = new EmbedBuilder(this.help());
                preMessage.setFooter(author.getName(), author.getAvatarUrl());
                message = preMessage.build();
            } else {
                message = this.help();
            }
            channel.sendMessage(message).queue();
        } else {
            Commands[] commands = Commands.values();


            for (Commands c : commands) {
                //TODO
                /*
                if (c.getCommand().getLabel().equalsIgnoreCase(args[1])) {
                    if (member != null) {

                        EmbedBuilder preMessage = new EmbedBuilder(c.getCommand().getHelp());
                        preMessage.setFooter(member.getUser().getName(), member.getUser().getAvatarUrl());
                        message = preMessage.build();
                    } else {
                        message = c.getCommand().getHelp();
                    }
                    channel.sendMessage(message).queue();
                }

                 */
            }
            if (message == null) {
                channel.sendMessage("La commande `" + args.get(0) + "` n'existe pas").queue();
            }
        }
    }
}
