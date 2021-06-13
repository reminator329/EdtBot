package reminator.EdtBot.edt;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.json.JSONArray;
import reminator.EdtBot.bot.BotEmbed;
import reminator.EdtBot.utils.CoursParser;
import java.util.*;

public abstract class GestionEdt {

    protected ArrayList<Cours> nextCourses = new ArrayList<>();
    protected ArrayList<Cours> courses;
    protected String csv;

    public GestionEdt() {
        courses = new ArrayList<>();
    }

    public ArrayList<Cours> getNextCourse() {
        courses.clear();
        updateCsv();
        updateEdt();
        nextCourses.clear();
        return getNextCourses();
    }

    private ArrayList<Cours> getNextCourses() {

        Date date = new Date();

        TreeSet<Cours> nextCourses = new TreeSet<>(new TreeSet<>(courses).tailSet(new Cours(null, new Date(date.getTime() - 500 * 3600), null, null)));
        nextCourses.removeIf(Cours::isNotAccepted);
        return new ArrayList<>(nextCourses.headSet(nextCourses.first(), true));
    }

    public TypeCourse getTypeCours(Cours cours) {

        TypeCourse type = new TypeCourse();

        try {
            Date date = cours.getStart();

            String jour = new CoursParser().getJour(csv, date, cours);
            if (jour != null) {
                type = new CoursParser().getTypeCours(jour, date);
            }

        } catch (NullPointerException ignored) {
        }
        return type;
    }

    protected abstract void updateEdt();

    protected abstract void updateCsv();

    protected void ajoutCourss(JSONArray courss, String groupe) {

        for (int i = 0; i < courss.length(); i++) {
            Cours c = new CoursParser(groupe).parse(courss.getJSONObject(i));
            TypeCourse type = getTypeCours(c);
            c.setType(type);
            courses.add(c);
        }
    }

    public void printCourse(Cours cours, MessageChannel channel) {

        EmbedBuilder builder = BotEmbed.COURSE.getBuilder(cours);

        String groupe = cours.getGroupe();
        if ("0".equals(groupe)) {
            builder.setTitle("Prochain cours");
        } else {
            builder.setTitle("Prochain cours, groupe " + groupe);
        }
        builder.appendDescription(cours.getSummary());

        channel.sendMessage(builder.build()).queue();
    }
}
