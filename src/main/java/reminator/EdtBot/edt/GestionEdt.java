package reminator.EdtBot.edt;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.json.JSONArray;
import reminator.EdtBot.bot.BotEmbed;
import reminator.EdtBot.utils.CoursParser;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
        Date date = new Date();
        updateEdt(date.getTime(), date.getTime() + 1000L * 3600 * 24 * 7 * 20);
        nextCourses.clear();
        return getNextCourses();
    }

    protected abstract ArrayList<Cours> getNextCourses();

    public Map<Integer, ArrayList<Cours>> getNextWeek() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(getNextCourse().get(0).getStart());
        System.out.println(cal.getTime());

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        Date min = cal.getTime();

        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        Date max = cal.getTime();

        System.out.println(min);
        System.out.println(max);

        updateEdt(min.getTime(), max.getTime());

        Map<Integer, ArrayList<Cours>> week = new TreeMap<>();
        for (int i : new int[]{Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY}) {
            ArrayList<Cours> day = new ArrayList<>();
            courses.stream().filter(c -> {
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(c.getStart());
                return i == cal2.get(Calendar.DAY_OF_WEEK);
            }).sorted().forEach(day::add);

            if (this instanceof GestionEdt1A)
                day.removeIf(c -> c.isNotAccepted(1));
            else if (this instanceof GestionEdt2A)
                day.removeIf(c -> c.isNotAccepted(2));
            else if (this instanceof GestionEdt3A)
                day.removeIf(c -> c.isNotAccepted(3));

            week.put(i, day);
        }

        return week;
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

    protected void updateEdt(long min, long max) {
        courses.clear();
    }

    protected abstract void updateCsv();

    protected void ajoutCourss(JSONArray courss, String groupe) {

        for (int i = 0; i < courss.length(); i++) {
            Cours c = new CoursParser(groupe).parse(courss.getJSONObject(i));
            TypeCourse type = getTypeCours(c);
            c.setType(type);
            if (c.getStart() != null)
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

    public void printWeek(Map<Integer, ArrayList<Cours>> week, MessageChannel channel) {

        System.out.println(week);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat jour = new SimpleDateFormat("EEEEEEEEEEEEEEEE dd MMMMMMMMMM", Locale.FRANCE);
        SimpleDateFormat heure = new SimpleDateFormat("H:mm", Locale.FRANCE);
        for (Map.Entry<Integer, ArrayList<Cours>> entry : week.entrySet()) {
            if (entry.getValue().size() != 0) {
                EmbedBuilder builder = BotEmbed.BASE.getBuilder(null);
                cal.set(Calendar.DAY_OF_WEEK, entry.getKey());

                builder.setTitle(jour.format(cal.getTime()));
                builder.setDescription(entry.getValue().stream().map(c -> String.format("**%s - %s** %s", heure.format(c.getStart()), heure.format(c.getEnd()), c.getSummary())).collect(Collectors.joining("\n")));
                channel.sendMessage(builder.build()).queue();
            }
        }
    }
}
