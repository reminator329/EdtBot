package reminator.EdtBot.edt;

import org.jetbrains.annotations.NotNull;

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

    public boolean isAccepted(int annee) {
        switch (annee) {
            case 1 -> {
                return getSummary().contains("ELU") ||
                        getSummary().contains("**EXAMEN**") ||
                        getSummary().contains("ACCUEIL") ||
                        getSummary().contains("Réunion Information") ||
                        getSummary().contains("UPSSITECH");
            }
            case 2 -> {
                return getSummary().contains("EMU") ||
                        getSummary().contains("Réunion de rentrée") ||
                        getSummary().contains("Jeu d'accueil Ecole") ||
                        getSummary().contains("TER");
            }
            case 3 -> {
                return getSummary().contains("EIU") ||
                        getSummary().contains("Projet") ||
                        getSummary().contains("PGE") ||
                        getSummary().contains("Accessibilité") ||
                        getSummary().contains("Réunion de rentrée");
            }
            default -> {
                return false;
            }
        }
    }

    public boolean isNotAccepted(int annee) {
        return !isAccepted(annee);
    }
}
