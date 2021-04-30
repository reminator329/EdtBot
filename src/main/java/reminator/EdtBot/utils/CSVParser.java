package reminator.EdtBot.utils;

import reminator.EdtBot.edt.Cours;
import reminator.EdtBot.edt.TypeCourse;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CSVParser {


    public static String getJour(String csv, Date date, Cours cours) {

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

    public static TypeCourse getTypeCours(String jour, Date date) {

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
}
