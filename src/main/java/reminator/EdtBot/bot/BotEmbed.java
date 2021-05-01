package reminator.EdtBot.bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.RichPresence;
import net.dv8tion.jda.api.entities.User;
import reminator.EdtBot.edt.Cours;
import reminator.EdtBot.edt.TypeCourse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public enum BotEmbed {

    BASE(o -> new EmbedBuilder().setColor(EdtBot.color)),

    BASE_USER(o -> {
        if (o == null || !(o instanceof User))
            return BotEmbed.BASE.getBuilder(null);
        User user = (User) o;
        return BotEmbed.BASE.getBuilder(null).setFooter(user.getName(), user.getAvatarUrl());
    }),

    SPOTIFY(o -> {
        if (!(o instanceof Member))
            return BotEmbed.BASE.getBuilder(null);
        Member member = (Member) o;
        EmbedBuilder builder = BotEmbed.BASE.getBuilder(null);
        List<Activity> activities = member.getActivities();
        for (Activity a : activities) {
            if (a.getName().equalsIgnoreCase("Spotify")) {
                addActivitySpotify(builder, a, member);
            }
        }
        return builder;
    }),

    COURSE(o -> {
        if (!(o instanceof Cours))
            return BotEmbed.BASE.getBuilder(null);
        Cours cours = (Cours) o;
        EmbedBuilder builder = BotEmbed.BASE.getBuilder(null);
        addCourse(builder, cours);
        return builder;
    });


    private static final SimpleDateFormat dateFormat2 = new SimpleDateFormat("'Le 'dd/MM' à 'HH:mm");
    private final Function<Object, EmbedBuilder> f;

    BotEmbed(Function<Object, EmbedBuilder> function) {
        f = function;
    }

    public EmbedBuilder getBuilder(Object o) {
        return f.apply(o);
    }

    private static void addActivitySpotify(EmbedBuilder builder, Activity a, Member member) {
        RichPresence rp = a.asRichPresence();
        if (rp != null) {
            try {
                builder.setImage(Objects.requireNonNull(rp.getLargeImage()).getUrl());
            } catch (NullPointerException ignored) {}
            String message = member.getUser().getName() + " écoute " + rp.getDetails() + " de " + rp.getState();
            builder.setFooter(message, member.getUser().getAvatarUrl());
        }
    }

    private static void addCourse(EmbedBuilder builder, Cours cours) {

        builder.addField("Date", dateFormat2.format(new Date(cours.getStart().getTime())), false);
        TypeCourse type = cours.getTypeCourse();
        String modality = type.getModality();
        if (modality != null) {
            builder.addField("Type", modality, false);
        }
        String lien = type.getLien();
        System.out.println(lien);
        if (lien != null && !lien.equals("")) {
            if (lien.contains("discord")) {
                builder.addField("Discord", lien, false);
            } else if (lien.contains("zoom")) {
                builder.addField("Zoom", lien, false);
            } else {
                builder.addField("Informations complémentaires", lien, false);
            }
        }
    }
}
