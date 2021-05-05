package reminator.EdtBot.edt;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.json.JSONArray;
import org.json.JSONObject;
import reminator.EdtBot.bot.BotEmbed;
import reminator.EdtBot.bot.EdtBot;
import reminator.EdtBot.edt.enums.Edt;
import reminator.EdtBot.edt.enums.Liens;
import reminator.EdtBot.utils.CoursParser;
import reminator.EdtBot.utils.HTTPRequest;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GestionEdt {

    ArrayList<Cours> nextCourses = new ArrayList<>();
    ArrayList<Cours> courses;
    String csv;
    private String edt01;
    private String edt02;
    private String edt1;
    private String edt2;

    public GestionEdt() {
        courses = new ArrayList<>();
    }

    public ArrayList<Cours> getNextCourse() {
        updateEdt();
        nextCourses.clear();
        return getNextCourses();
    }

    private ArrayList<Cours> getNextCourses() {

        Date date = new Date();


        TreeSet<Cours> nextCourses = new TreeSet<>(new TreeSet<>(courses).tailSet(new Cours(null, new Date(date.getTime() - 500 * 3600), null, null)));
        nextCourses.removeIf(cours -> !cours.getSummary().contains("ELU"));
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

    private void updateEdt() {
        courses.clear();
        updateCsv();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Date date = new Date();

        String timeMin = dateFormat.format(date).replace(":", "%3A").replace("+", "%2B");
        String timeMax = dateFormat.format(new Date(date.getTime() + 1000 * 3600 * 24 * 14)).replace(":", "%3A").replace("+", "%2B");

        try {
            this.edt01 = Edt.EDT01.getHTTPRequest(timeMin, timeMax).GET();
            this.edt02 = Edt.EDT02.getHTTPRequest(timeMin, timeMax).GET();
            this.edt1 = Edt.EDT1.getHTTPRequest(timeMin, timeMax).GET();
            this.edt2 = Edt.EDT2.getHTTPRequest(timeMin, timeMax).GET();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jEdt01 = new JSONObject(edt01);
        JSONObject jEdt02 = new JSONObject(edt02);
        JSONObject jEdt1 = new JSONObject(edt1);
        JSONObject jEdt2 = new JSONObject(edt2);

        JSONArray jCourss01 = jEdt01.getJSONArray("items");
        JSONArray jCourss02 = jEdt02.getJSONArray("items");
        JSONArray jCourss1 = jEdt1.getJSONArray("items");
        JSONArray jCourss2 = jEdt2.getJSONArray("items");

        ajoutCourss(jCourss01, "0");
        ajoutCourss(jCourss02, "0");
        ajoutCourss(jCourss1, "1");
        ajoutCourss(jCourss2, "2");
    }

    private void updateCsv() {
        try {
            csv = new HTTPRequest(Liens.CSV.getUrl()).GET();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ajoutCourss(JSONArray courss, String groupe) {

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
