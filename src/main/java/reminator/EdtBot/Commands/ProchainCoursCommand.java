package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import reminator.EdtBot.Categories.Category;
import reminator.EdtBot.Categories.enums.Categories;
import reminator.EdtBot.Commands.argument.Argument;
import reminator.EdtBot.Commands.argument.Arguments;
import reminator.EdtBot.Commands.genericEvent.commandEvent.CommandEvent;
import reminator.EdtBot.edt.*;
import reminator.EdtBot.edt.gestionEdt.GestionEdt;
import reminator.EdtBot.edt.gestionEdt.GestionEdt1A;
import reminator.EdtBot.edt.gestionEdt.GestionEdt2A;
import reminator.EdtBot.edt.gestionEdt.GestionEdt3A;

import java.util.ArrayList;
import java.util.List;

public class ProchainCoursCommand implements Command {
    private final Argument<String> annee = new Argument<>(String.class, "annee", "(1|2|3)A");

    @Override
    public List<Argument<?>> getArguments() {
        return List.of(annee);
    }

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
    public void execute(CommandEvent event, User author, MessageChannel channel, Arguments arguments) {

        String annee = arguments.get(this.annee);

        GestionEdt gestionEdt;

        switch (annee) {
            case "1A", "1" -> gestionEdt = new GestionEdt1A();
            case "2A", "2" -> gestionEdt = new GestionEdt2A();
            case "3A", "3" -> gestionEdt = new GestionEdt3A();
            default -> {
                channel.sendMessage(annee + " n'est pas un paramètre valide.").queue();
                return;
            }
        }
        ArrayList<Cours> courss = gestionEdt.getNextCourse();
        for (Cours cours : courss) {
            gestionEdt.printCourse(cours, channel);
        }
    }

    @Override
    public MessageEmbed.Field[] getExtraFields() {
        return new MessageEmbed.Field[]{
                new MessageEmbed.Field("Exemple", "edt!prochain-cours 2A", false),
        };
    }
}
