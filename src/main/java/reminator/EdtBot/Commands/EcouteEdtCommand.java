package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import reminator.EdtBot.Categories.Category;
import reminator.EdtBot.Categories.enums.Categories;
import reminator.EdtBot.Commands.argument.Argument;
import reminator.EdtBot.Commands.argument.Arguments;
import reminator.EdtBot.Commands.genericEvent.commandEvent.CommandEvent;
import reminator.EdtBot.utils.EcouteursEdt;

import java.util.List;

public class EcouteEdtCommand implements Command {
    private final Argument<String> annee = new Argument<>(String.class, "annee", "(1|2|3)A");

    @Override
    public List<Argument<?>> getArguments() {
        return List.of(annee);
    }

    @Override
    public String getName() {
        return "écoute edt";
    }

    @Override
    public Category getCategory() {
        return Categories.EDT.getCategory();
    }

    @Override
    public String getLabel() {
        return "ecoute-edt";
    }

    @Override
    public String[] getAlliass() {
        return new String[]{"ecoute", "ee", "e-e", "edt-auto", "e-a", "auto"};
    }

    @Override
    public String getDescription() {
        return "Permet d'envoyer les détails du prochain cours automatiquement.\n\nQuand la commande est exécuté, elle active ou désactive l'envoi des messages.\nLes messages seront envoyés dans le salon où la commande a été exécutée.";
    }

    @Override
    public String getShortDescription() {
        return "Permet d'envoyer les détails du prochain cours automatiquement.";
    }

    @Override
    public void execute(CommandEvent event, User author, MessageChannel channel, Arguments arguments) {

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR) && !author.getName().equalsIgnoreCase("reminator392")) {
            channel.sendMessage("Tu n'as pas la permission pour faire cette commande.").queue();
            return;
        }

        String anneeString = arguments.get(this.annee);

        int annee;
        switch (anneeString) {
            case "1A", "1" -> annee = 1;
            case "2A", "2" -> annee = 2;
            case "3A", "3" -> annee = 3;
            case "stop" -> {
                channel.sendMessage("Arrêt de la commande").queue();
                new EcouteursEdt().supprimerEcouteur(channel.getId());
                return;
            }
            default -> {
                channel.sendMessage(anneeString + " n'est pas un paramètre valide.").queue();
                return;
            }
        }

        channel.sendMessage("Début de la commande").queue();
        new EcouteursEdt().ajoutEcouteur(channel.getId(), annee);
    }
}
