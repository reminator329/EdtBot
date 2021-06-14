package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import reminator.EdtBot.Categories.Category;
import reminator.EdtBot.Categories.enums.Categories;
import reminator.EdtBot.edt.Cours;
import reminator.EdtBot.edt.GestionEdt;
import reminator.EdtBot.edt.GestionEdt1A;
import reminator.EdtBot.edt.GestionEdt2A;
import reminator.EdtBot.utils.EcouteursEdt;

import java.util.*;
import java.util.List;

public class EcouteEdtCommand implements Command {

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
    public void execute(GuildMessageReceivedEvent event, User author, MessageChannel channel, List<String> args) {
        if (event.getMember() == null) return;
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR) && !event.getAuthor().getName().equalsIgnoreCase("reminator392")) {
            channel.sendMessage("Tu n'as pas la permission pour faire cette commande.").queue();
            return;
        }

        if (args.size() == 0) {
            channel.sendMessage("Commande mal utilisée, voir `edt!help ecoute-edt`.").queue();
            return;
        }
        int annee;
        switch (args.get(0)) {
            case "1A", "1" -> annee = 1;
            case "2A", "2" -> annee = 2;
            case "3A", "3" -> annee = 3;
            case "stop" -> {
                channel.sendMessage("Arrêt de la commande").queue();
                new EcouteursEdt().supprimerEcouteur(channel.getId());
                return;
            }
            default -> {
                channel.sendMessage(args.get(0) + " n'est pas un paramètre valide.").queue();
                return;
            }
        }

        channel.sendMessage("Début de la commande").queue();
        new EcouteursEdt().ajoutEcouteur(channel.getId(), annee);
    }
}
