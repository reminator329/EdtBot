package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import reminator.EdtBot.Categories.Category;
import reminator.EdtBot.Categories.enums.Categories;
import reminator.EdtBot.edt.Cours;
import reminator.EdtBot.edt.GestionEdt;
import reminator.EdtBot.edt.GestionEdt1A;
import reminator.EdtBot.edt.GestionEdt2A;

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
    public void execute(GuildMessageReceivedEvent event, User author, MessageChannel channel, List<String> args) {
        GestionEdt gestionEdt;
        if (args.size() < 2) {
            channel.sendMessage("Command mal utilisé, voir `edt!help prochain-cours`.").queue();
            return;
        }
        switch (args.get(1)) {
            case "1A" -> gestionEdt = new GestionEdt1A();
            case "2A" -> gestionEdt = new GestionEdt2A();
            default -> {
                channel.sendMessage(args.get(1) + " n'est pas un paramètre valide.").queue();
                return;
            }
        }
        ArrayList<Cours> courss = gestionEdt.getNextCourse();
        for (Cours cours : courss) {
            gestionEdt.printCourse(cours, channel);
        }
    }
}
