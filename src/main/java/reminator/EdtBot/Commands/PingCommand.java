package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import reminator.EdtBot.bot.EdtBot;

import java.awt.*;
import java.util.List;

public class PingCommand implements Command {

/*
    @Override
    public MessageEmbed setHelp() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.RED);
        builder.setTitle("Commande ping");
        builder.appendDescription("Répond pong !");
        builder.addField("Signature", "`edt!ping`", false);
        return builder.build();
    }
*/
    @Override
    public String getLabel() {
        return "ping";
    }

    @Override
    public String[] getAlliass() {
        return new String[]{"p"};
    }

    @Override
    public String getDescription() {
        return "Répond pong et affiche la musique écoutée actuellement sur spotify.";
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, User author, MessageChannel channel, List<String> args) {
        Member member = event.getMember();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.RED);
        builder.setTitle("Pong !");

        if (member != null) {
            List<Activity> activities = member.getActivities();
            for (Activity a : activities) {
                if (a.getName().equalsIgnoreCase("Spotify")) {
                    RichPresence rp = a.asRichPresence();
                    if (rp != null) {
                        try {
                            builder.setImage(rp.getLargeImage().getUrl());
                        } catch (NullPointerException ignored) {}
                        System.out.println(rp.getDetails());
                        String message = member.getUser().getName() + " écoute " + rp.getDetails() + " de " + rp.getState();
                        builder.setFooter(message, member.getUser().getAvatarUrl());
                    }
                }
            }
        }

        channel.sendMessage(builder.build()).queue();
    }
}
