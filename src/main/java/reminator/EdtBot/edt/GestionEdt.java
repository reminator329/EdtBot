package reminator.EdtBot.edt;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.json.JSONArray;
import org.json.JSONObject;
import reminator.EdtBot.edt.enums.Edt;
import reminator.EdtBot.edt.enums.Liens;
import reminator.EdtBot.utils.HTTPRequest;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GestionEdt {

    private String edt01;
    private String edt02;
    private String edt1;
    private String edt2;

    ArrayList<Cours> prochainCours = new ArrayList<>();
    ArrayList<Cours> courses;
    String csv;

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
                        } else if ((new Date (date.getTime() - 500 * 3600)).compareTo(nouveauPCours) < 0 && nouveauPCours.compareTo(pCours) < 0) {
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

    public String[] getTypeCours(Cours cours) {
        String[] type = new String[2];
        try {

            SimpleDateFormat formatJour = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatHeure = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat formatCours = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

            Date date = formatCours.parse(cours.getStart());

            String[] joursListe = csv.split("\n");
            String jour = null;
            for (String s : joursListe) {
                String[] jourList = s.split(",");
                if (jourList.length == 0) continue;
                if (formatJour.format(date).equals(jourList[0])) {
                    if (cours.getGroupe().equals(jourList[1])) {
                        jour = s;
                    }
                }
            }

            if (jour != null) {
                String[] jourList = jour.split(",");
                for (int i=2; i<jourList.length; i+=3) {
                    String heure = jourList[i];
                    if (heure.equals(formatHeure.format(date))) {
                        if (jourList.length > i+1) {
                            type[0] = jourList[i+1];
                        } else {
                            type[0] = null;
                        }
                        if (jourList.length > i+2) {
                            type[1] = jourList[i + 2];
                        } else {
                            type[1] = null;
                        }
                        break;
                    }
                }
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

        for (int i=0; i<courss.length(); i++) {
            Cours c = new Cours(courss.getJSONObject(i), groupe);
            String[] type = getTypeCours(c);
            c.setType(type[0]);
            c.setLien(type[1]);
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
        String type = cours.getType();
        if (type != null) {
            builder.addField("Type", cours.getType(), false);
        }
        String lien = cours.getLien();
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
