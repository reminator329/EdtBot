package reminator.EdtBot.edt;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Cours implements Comparable<Cours> {

    private final String summary;
    private final Date start;
    private final Date end;
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

    @Override
    public int compareTo(@NotNull Cours o) {
        return start.compareTo(o.getStart());
    }

    @Override
    public String toString() {
        return "Cours{" +
                "summary='" + summary + '\'' +
                ", start=" + start +
                '}';
    }

    public boolean isAccepted() {
        return getSummary().contains("ELU") || getSummary().contains("**EXAMEN**") || getSummary().contains("PRESENTIEL") || getSummary().contains("DISTANCIEL") || getSummary().contains("PRESENTTIEL");
    }

    public boolean isNotAccepted() {
        return !isAccepted();
    }
}
