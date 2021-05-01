package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import reminator.EdtBot.Categories.Category;
import reminator.EdtBot.Categories.enums.Categories;
import reminator.EdtBot.edt.Cours;
import reminator.EdtBot.edt.GestionEdt;

import java.util.*;
import java.util.List;

public class EcouteEdtCommand implements Command {

    private final List<MessageChannel> ecoutes = new ArrayList<>();
    private Timer timer;

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
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            channel.sendMessage("Tu n'as pas la permission pour faire cette commande.").queue();
            return;
        }

        if (ecoutes.contains(channel)) {
            channel.sendMessage("Arrêt de la commande").queue();
            ecoutes.remove(channel);
            timer.cancel();
            timer.purge();
        } else {
            channel.sendMessage("Début de la commande").queue();
            ecoutes.add(channel);
            lancerTimer(channel);
        }
    }

    private void lancerTimer(MessageChannel channel) {

        timer = new Timer();
        final ArrayList<Cours>[] cours = new ArrayList[]{new ArrayList<Cours>()};
        final ArrayList<Cours>[] pCours = new ArrayList[]{new ArrayList<Cours>()};
        GestionEdt gestionEdt = new GestionEdt();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                pCours[0].clear();
                pCours[0].addAll(gestionEdt.getNextCourse());
                if (cours[0].size() == 0 || !cours[0].get(0).getSummary().equals(pCours[0].get(0).getSummary())) {
                    cours[0].clear();
                    cours[0].addAll(pCours[0]);
                    for (Cours c : cours[0]) {
                        gestionEdt.printCourse(c, channel);
                    }
                }
            }
        }, 0, 1000 * 60/*500 * 3600*/);
    }
}
