package reminator.EdtBot.edt.gestionEdt;

import org.json.JSONArray;
import org.json.JSONObject;
import reminator.EdtBot.edt.Cours;
import reminator.EdtBot.edt.enums.Edt;
import reminator.EdtBot.edt.enums.Liens;
import reminator.EdtBot.utils.HTTPRequest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;

public class GestionEdt3A extends GestionEdt {

    private String edt3A;
    private String edt3A_G1;
    private String edt3A_G2;

    @Override
    protected ArrayList<Cours> getNextCourses() {

        Date date = new Date();

        TreeSet<Cours> nextCourses = new TreeSet<>(courses);
        nextCourses.removeIf(c -> c.isNotAccepted(3));

        nextCourses = new TreeSet<>(nextCourses.tailSet(new Cours(null, new Date(date.getTime() - 500 * 3600), null, null)));
        return new ArrayList<>(nextCourses.headSet(nextCourses.first(), true));
    }

    @Override
    protected void updateEdt(long min, long max) {
        super.updateEdt(min, max);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

        String timeMin = dateFormat.format(new Date(min)).replace(":", "%3A").replace("+", "%2B");
        String timeMax = dateFormat.format(new Date(max)).replace(":", "%3A").replace("+", "%2B");

        try {
            this.edt3A = Edt.EDT_3A_C.getHTTPRequest(timeMin, timeMax).GET();
            this.edt3A_G1 = Edt.EDT_3A_G1.getHTTPRequest(timeMin, timeMax).GET();
            this.edt3A_G2 = Edt.EDT_3A_G2.getHTTPRequest(timeMin, timeMax).GET();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jEdt01 = new JSONObject(edt3A);
        JSONObject jEdt02 = new JSONObject(edt3A_G1);
        JSONObject jEdt03 = new JSONObject(edt3A_G2);

        JSONArray jCourss01 = jEdt01.getJSONArray("items");
        JSONArray jCourss02 = jEdt02.getJSONArray("items");
        JSONArray jCourss03 = jEdt03.getJSONArray("items");

        ajoutCourss(jCourss01, "0");
        ajoutCourss(jCourss02, "1");
        ajoutCourss(jCourss03, "2");
    }

    @Override
    protected void updateCsv() {
        // TODO Créer un csv
        try {
            csv = new HTTPRequest(Liens.CSV_1A.getUrl()).GET();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
