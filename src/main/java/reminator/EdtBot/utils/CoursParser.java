package reminator.EdtBot.utils;

import org.json.JSONException;
import org.json.JSONObject;
import reminator.EdtBot.edt.Cours;
import reminator.EdtBot.edt.TypeCourse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CoursParser {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    private final String groupe;

    public CoursParser() {
        this.groupe = "0";
    }

    public CoursParser(String groupe) {
        this.groupe = groupe;
    }

    public String getJour(String csv, Date date, Cours cours) {

        SimpleDateFormat formatJour = new SimpleDateFormat("dd/MM/yyyy");

        String[] joursListe = csv.split("\n");
        for (String s : joursListe) {
            String[] jourList = s.split(",");
            if (jourList.length == 0) continue;
            if (formatJour.format(date).equals(jourList[0])) {
                if (cours.getGroupe().equals(jourList[1])) {
                    return s;
                }
            }
        }
        return null;
    }

    public TypeCourse getTypeCours(String jour, Date date) {

        SimpleDateFormat formatHeure = new SimpleDateFormat("HH:mm:ss");
        String modality = null;
        String lien = null;

        String[] jourList = jour.split(",");
        for (int i = 2; i < jourList.length; i += 3) {
            String heure = jourList[i];
            if (heure.equals(formatHeure.format(date))) {
                if (jourList.length > i + 1) {
                    modality = jourList[i + 1];
                }
                if (jourList.length > i + 2) {
                    lien = jourList[i + 2];
                }
                break;
            }
        }
        return new TypeCourse(modality, lien);
    }

    public Cours parse(JSONObject jCours) {

        String summary = jCours.getString("summary");
        Date start = null;
        Date end = null;
        try {
            start = dateFormat.parse(jCours.getJSONObject("start").getString("dateTime"));
            end = dateFormat.parse(jCours.getJSONObject("end").getString("dateTime"));
        } catch (JSONException | ParseException ignored) {
        }
        return new Cours(summary, start, end, groupe);
    }
}
