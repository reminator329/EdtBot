package reminator.EdtBot.edt;

import org.json.JSONException;
import org.json.JSONObject;

public class Cours {

    private final String summary;
    private String start;
    private String end;
    private TypeCourse typeCourse;
    private String groupe;

    public Cours(JSONObject jCours, String groupe) {
        this.summary = jCours.getString("summary");
        try {
            this.start = jCours.getJSONObject("start").getString("dateTime");
            this.end = jCours.getJSONObject("end").getString("dateTime");
        } catch (JSONException ignored) {
            this.start = null;
            this.end = null;
        }
        this.groupe = groupe;
    }

    public String getSummary() {
        return summary;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    public void setType(TypeCourse type) {
        this.typeCourse = type;
    }

    public TypeCourse getTypeCourse() {
        return typeCourse;
    }
}
