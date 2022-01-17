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
import java.util.Comparator;
import java.util.Date;
import java.util.TreeSet;

public class GestionEdt2A extends GestionEdt {

    private String edt2A;
    private String edt2ATP;

    @Override
    protected ArrayList<Cours> getNextCourses() {

        Date date = new Date();
      
        TreeSet<Cours> nextCourses = new TreeSet<>(courses);
        nextCourses.removeIf(c -> c.isNotAccepted(2));
      
        nextCourses = new TreeSet<>(nextCourses.tailSet(new Cours("ZZZZZZZZZZZ", new Date(date.getTime() - 1000 * 60 * 15), null, null)));
        return new ArrayList<>(nextCourses.headSet(new Cours("ZZZZZZZZZZZ", nextCourses.first().getStart(), null, null), true));
    }

    @Override
    protected void updateEdt(long min, long max) {
        super.updateEdt(min, max);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

        String timeMin = dateFormat.format(new Date(min)).replace(":", "%3A").replace("+", "%2B");
        String timeMax = dateFormat.format(new Date(max)).replace(":", "%3A").replace("+", "%2B");

        try {
            this.edt2A = Edt.EDT_2A.getHTTPRequest(timeMin, timeMax).GET();
            this.edt2ATP = Edt.EDT_2A_TP.getHTTPRequest(timeMin, timeMax).GET();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jEdt01 = new JSONObject(edt2A);
        JSONObject jEdt02 = new JSONObject(edt2ATP);

        JSONArray jCourss01 = jEdt01.getJSONArray("items");
        JSONArray jCourss02 = jEdt02.getJSONArray("items");

        ajoutCourss(jCourss01, "0");
        ajoutCourss(jCourss02, "0");
    }

    @Override
    protected void updateCsv() {
        // TODO créer un CSV
        try {
            csv = new HTTPRequest(Liens.CSV_1A.getUrl()).GET();
            System.out.println(csv);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
