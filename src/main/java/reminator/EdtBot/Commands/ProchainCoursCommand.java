package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import reminator.EdtBot.bot.EdtBot;
import reminator.EdtBot.edt.Cours;
import reminator.EdtBot.edt.Edt;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ProchainCoursCommand extends Command {

    public ProchainCoursCommand() {
        this.setPrefix(EdtBot.prefix);
        this.setLabel("prochain-cours");
        this.setHelp(setHelp());
    }

    @Override
    public MessageEmbed setHelp() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.RED);
        builder.setTitle("Commande prochain-cours");
        builder.appendDescription("Donne les détail du prochain cours.");

        builder.addField("Signature", "`r!prochain-cours`", false);

        return builder.build();
    }

    @Override
    public void executerCommande(GuildMessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("'Le 'dd/MM' à 'HH:mm");

        Edt edt = new Edt();
        ArrayList<Cours> courss = edt.getNextCourse();
        for (Cours cours : courss) {
            edt.printCourse(cours, channel);
        }
    }
}
