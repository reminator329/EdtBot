package reminator.EdtBot.edt;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.json.JSONArray;
import org.json.JSONObject;
import reminator.EdtBot.edt.enums.Edt;
import reminator.EdtBot.edt.enums.Liens;
import reminator.EdtBot.utils.CSVParser;
import reminator.EdtBot.utils.HTTPRequest;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GestionEdt {

    ArrayList<Cours> prochainCours = new ArrayList<>();
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Date date = new Date();

        prochainCours.clear();
        for (Cours c : courses) {
            String summary = c.getSummary();
            if (summary.contains("ELU")) {
                if (prochainCours.size() == 0) {
                    prochainCours.add(c);
                } else {
                    try {
                        Date nouveauPCours = dateFormat.parse(c.getStart());
                        Date pCours = dateFormat.parse(prochainCours.get(0).getStart());
                        if (nouveauPCours.compareTo(pCours) == 0) {
                            prochainCours.add(c);
                        } else if ((new Date(date.getTime() - 500 * 3600)).compareTo(nouveauPCours) < 0 && nouveauPCours.compareTo(pCours) < 0) {
                            prochainCours.clear();
                            prochainCours.add(c);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return prochainCours;
    }

    public TypeCourse getTypeCours(Cours cours) {

        TypeCourse type = null;

        try {

            SimpleDateFormat formatCours = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

            Date date = formatCours.parse(cours.getStart());

            String jour = CSVParser.getJour(csv, date, cours);

            if (jour != null) {
                type = CSVParser.getTypeCours(jour, date);
            }

        } catch (ParseException | NullPointerException ignored) {
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
            Cours c = new Cours(courss.getJSONObject(i), groupe);
            TypeCourse type = getTypeCours(c);
            c.setType(type);
            courses.add(c);
        }
    }

    public void printCourse(Cours cours, MessageChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("'Le 'dd/MM' à 'HH:mm");

        builder.setColor(Color.RED);

        String groupe = cours.getGroupe();
        if ("0".equals(groupe)) {
            builder.setTitle("Prochain cours");
        } else {
            builder.setTitle("Prochain cours, groupe " + groupe);
        }
        builder.appendDescription(cours.getSummary());

        try {
            builder.addField("Date", dateFormat2.format(new Date(dateFormat1.parse(cours.getStart()).getTime())), false);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
                builder.addField("Lien", lien, false);
            }
        }
        channel.sendMessage(builder.build()).queue();
    }
}
