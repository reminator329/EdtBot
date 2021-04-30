package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import reminator.EdtBot.bot.EdtBot;
import reminator.EdtBot.edt.Cours;
import reminator.EdtBot.edt.GestionEdt;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProchainCoursCommand implements Command {
/*
    @Override
    public MessageEmbed setHelp() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.RED);
        builder.setTitle("Commande prochain-cours");
        builder.appendDescription("Donne les détail du prochain cours.");

        builder.addField("Signature", "`edt!prochain-cours`", false);
        builder.addField("Alias", "`edt!next`", false);

        return builder.build();
    }
*/

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
    public void execute(GuildMessageReceivedEvent event, User author, MessageChannel channel, List<String> args) {
            GestionEdt gestionEdt = new GestionEdt();
            ArrayList<Cours> courss = gestionEdt.getNextCourse();
            for (Cours cours : courss) {
                gestionEdt.printCourse(cours, channel);
            }
    }
}
