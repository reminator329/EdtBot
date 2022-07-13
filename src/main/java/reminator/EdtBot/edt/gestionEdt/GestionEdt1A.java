package reminator.EdtBot.edt.gestionEdt;

import org.json.JSONArray;
import org.json.JSONObject;
import reminator.EdtBot.edt.Cours;
import reminator.EdtBot.edt.enums.Edt;
import reminator.EdtBot.edt.enums.Liens;
import reminator.EdtBot.utils.HTTPRequest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GestionEdt1A extends GestionEdt {

    private String edt01;
    private String edt02;
    private String edt1;
    private String edt2;
    private String edt3;


    @Override
    protected ArrayList<Cours> getNextCourses() {

        Date date = new Date();

        TreeSet<Cours> nextCourses = new TreeSet<>(courses);
        nextCourses.removeIf(c -> c.isNotAccepted(1));

        nextCourses = new TreeSet<>(nextCourses.tailSet(new Cours("ZZZZZZZZZZZ", new Date(date.getTime() - 1000 * 60 * 10), null, null)));
        return new ArrayList<>(nextCourses.headSet(new Cours("ZZZZZZZZZZZ", nextCourses.first().getStart(), null, null), true));
    }

    @Override
    protected void updateEdt(long min, long max) {
        super.updateEdt(min, max);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

        String timeMin = dateFormat.format(new Date(min)).replace(":", "%3A").replace("+", "%2B");
        String timeMax = dateFormat.format(new Date(max)).replace(":", "%3A").replace("+", "%2B");

        try {
            this.edt01 = Edt.EDT01.getHTTPRequest(timeMin, timeMax).GET();
            this.edt02 = Edt.EDT02.getHTTPRequest(timeMin, timeMax).GET();
            this.edt1 = Edt.EDT1.getHTTPRequest(timeMin, timeMax).GET();
            this.edt2 = Edt.EDT2.getHTTPRequest(timeMin, timeMax).GET();
            this.edt3 = Edt.EDT3.getHTTPRequest(timeMin, timeMax).GET();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jEdt01 = new JSONObject(edt01);
        JSONObject jEdt02 = new JSONObject(edt02);
        JSONObject jEdt1 = new JSONObject(edt1);
        JSONObject jEdt2 = new JSONObject(edt2);
        JSONObject jEdt3 = new JSONObject(edt3);

        JSONArray jCourss01 = jEdt01.getJSONArray("items");
        JSONArray jCourss02 = jEdt02.getJSONArray("items");
        JSONArray jCourss1 = jEdt1.getJSONArray("items");
        JSONArray jCourss2 = jEdt2.getJSONArray("items");
        JSONArray jCourss3 = jEdt3.getJSONArray("items");

        ajoutCourss(jCourss01, "0");
        ajoutCourss(jCourss02, "0");
        ajoutCourss(jCourss1, "1");
        ajoutCourss(jCourss2, "2");
        ajoutCourss(jCourss3, "3");
    }

    @Override
    protected void updateCsv() {
        /*
        try {
            csv = new HTTPRequest(Liens.CSV_1A.getUrl()).GET();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
