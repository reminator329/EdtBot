package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import reminator.EdtBot.Categories.Category;
import reminator.EdtBot.Categories.enums.Categories;
import reminator.EdtBot.edt.*;

import java.util.ArrayList;
import java.util.List;

public class ProchainCoursCommand implements Command {

    @Override
    public Category getCategory() {
        return Categories.EDT.getCategory();
    }

    @Override
    public String getLabel() {
        return "prochain-cours";
    }

    @Override
    public String[] getAlliass() {
        return new String[]{"next", "pc", "n", "p-c"};
    }

    @Override
    public String getDescription() {
        return "Donne les détails du prochain cours.";
    }

    @Override
    public String getSignature() {
        return Command.super.getSignature() + " <(1|2|3)A>";
    }

    @Override
    public MessageEmbed.Field[] getExtraFields() {
        return new MessageEmbed.Field[]{
                new MessageEmbed.Field("Exemple", "edt!prochain-cours 2A", false),
        };
    }

    @Override
    public void execute(GenericGuildMessageEvent event, User author, MessageChannel channel, List<String> args) {
        GestionEdt gestionEdt;
        if (args.size() == 0) {
            channel.sendMessage("Commande mal utilisée, voir `edt!help prochain-cours`.").queue();
            return;
        }
        switch (args.get(0)) {
            case "1A", "1" -> gestionEdt = new GestionEdt1A();
            case "2A", "2" -> gestionEdt = new GestionEdt2A();
            case "3A", "3" -> gestionEdt = new GestionEdt3A();
            default -> {
                channel.sendMessage(args.get(0) + " n'est pas un paramètre valide.").queue();
                return;
            }
        }
        ArrayList<Cours> courss = gestionEdt.getNextCourse();
        for (Cours cours : courss) {
            gestionEdt.printCourse(cours, channel);
        }
    }
}
