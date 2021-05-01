package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import reminator.EdtBot.Categories.Category;
import reminator.EdtBot.Categories.enums.Categories;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class PingCommand implements Command {

    @Override
    public Category getCategory() {
        return Categories.OTHER.getCategory();
    }

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
                    addActivitySpotify(builder, a, member);
                }
            }
        }

        channel.sendMessage(builder.build()).queue();
    }

    private void addActivitySpotify(EmbedBuilder builder, Activity a, Member member) {
        RichPresence rp = a.asRichPresence();
        if (rp != null) {
            try {
                builder.setImage(Objects.requireNonNull(rp.getLargeImage()).getUrl());
            } catch (NullPointerException ignored) {}
            String message = member.getUser().getName() + " écoute " + rp.getDetails() + " de " + rp.getState();
            builder.setFooter(message, member.getUser().getAvatarUrl());
        }
    }
}
