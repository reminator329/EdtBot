package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import reminator.EdtBot.Categories.Categorie;
import reminator.EdtBot.bot.Controller;
import reminator.EdtBot.bot.EdtBot;

import java.awt.*;
import java.util.ArrayList;

public class HelpCommand extends Command {

    private final Controller controller;

    public HelpCommand(Controller controller) {
        this.controller = controller;

        this.setPrefix(EdtBot.prefix);
        this.setLabel("help");
        this.setHelp(setHelp());
    }

    @Override
    public MessageEmbed setHelp() {
        EmbedBuilder builder = new EmbedBuilder();

        final String titre = "Liste des commandes du RémiBot";
        final String imageI = "https://image.flaticon.com/icons/png/512/1301/1301429.png";

        builder.setThumbnail(imageI);
        builder.setColor(Color.RED);
        builder.setTitle(titre, "https://www.remontees-mecaniques.net/");
        builder.appendDescription("Utilise `edt!help <commande>` pour plus d'informations sur une commande.");
        return builder.build();
    }

    private MessageEmbed help() {

        EmbedBuilder builder = new EmbedBuilder();
        ArrayList<Categorie> categories = controller.getCategories();

        final String titre = "Liste des commandes du RémiBot";
        final String imageI = "https://image.flaticon.com/icons/png/512/1301/1301429.png";

        builder.setThumbnail(imageI);
        builder.setColor(Color.RED);
        builder.setTitle(titre, "https://www.remontees-mecaniques.net/");
        builder.appendDescription("Utilise `r!help <commande>` pour plus d'informations sur une commande.");
        for (Categorie cat : categories) {
            String titreField = cat.getNom();
            StringBuilder descriptionField = new StringBuilder(cat.getDescription() + "\n");
            for (Command c : cat.getCommands()) {
                descriptionField.append("`").append(c.getLabel()).append("` ");
            }
            builder.addField(titreField, descriptionField.toString(), false);
        }
        return builder.build();
    }

    @Override
    public void executerCommande(GuildMessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();

        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Member member = event.getMember();

        MessageEmbed message = null;
        if (args.length == 1) {
            if (member != null) {
                EmbedBuilder preMessage = new EmbedBuilder(this.help());
                preMessage.setFooter(member.getUser().getName(), member.getUser().getAvatarUrl());
                message = preMessage.build();
            } else {
                message = this.help();
            }
            channel.sendMessage(message).queue();
        } else {
            ArrayList<Command> commands = this.controller.getCommands();

            for (Command c : commands) {
                if (c.getLabel().equalsIgnoreCase(args[1])) {
                    if (member != null) {
                        EmbedBuilder preMessage = new EmbedBuilder(c.getHelp());
                        preMessage.setFooter(member.getUser().getName(), member.getUser().getAvatarUrl());
                        message = preMessage.build();
                    } else {
                        message = c.getHelp();
                    }
                    channel.sendMessage(message).queue();
                }
            }
            if (message == null) {
                channel.sendMessage("La commande `" + args[1] + "` n'existe pas").queue();
            }
        }
    }
}
