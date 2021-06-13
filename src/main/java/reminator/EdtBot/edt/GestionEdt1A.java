package reminator.EdtBot.edt;

import org.json.JSONArray;
import org.json.JSONObject;
import reminator.EdtBot.edt.enums.Edt;
import reminator.EdtBot.edt.enums.Liens;
import reminator.EdtBot.utils.HTTPRequest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;

public class GestionEdt1A extends GestionEdt {

    private String edt01;
    private String edt02;
    private String edt1;
    private String edt2;


    @Override
    protected ArrayList<Cours> getNextCourses() {

        Date date = new Date();

        TreeSet<Cours> nextCourses = new TreeSet<>(new TreeSet<>(courses).tailSet(new Cours(null, new Date(date.getTime() - 500 * 3600), null, null)));
        nextCourses.removeIf(c -> c.isNotAccepted(1));
        return new ArrayList<>(nextCourses.headSet(nextCourses.first(), true));
    }

    @Override
    protected void updateEdt() {
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

    @Override
    protected void updateCsv() {
        try {
            csv = new HTTPRequest(Liens.CSV_1A.getUrl()).GET();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
