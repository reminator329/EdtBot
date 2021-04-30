package reminator.EdtBot.edt;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Cours {

    private final String summary;
    private Date start;
    private Date end;
    private TypeCourse typeCourse;
    private String groupe;

    public Cours(String summary, Date start, Date end, String groupe) {
        this.summary = summary;
        this.start = start;
        this.end = end;
        this.groupe = groupe;
    }

    public String getSummary() {
        return summary;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
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
