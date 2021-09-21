package reminator.EdtBot.edt;

import org.jetbrains.annotations.NotNull;
import reminator.EdtBot.edt.gestionEdt.GestionEdt;
import reminator.EdtBot.edt.gestionEdt.GestionEdt1A;
import reminator.EdtBot.edt.gestionEdt.GestionEdt2A;
import reminator.EdtBot.edt.gestionEdt.GestionEdt3A;

import java.util.Date;
import java.util.Objects;

public class Cours implements Comparable<Cours> {

    private final String summary;
    private final Date start;
    private final Date end;
    private TypeCourse typeCourse;
    private String groupe;

    private int position = 0;

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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int compareTo(@NotNull Cours course) {
        int start = this.getStart().compareTo(course.getStart());
        if (start == 0)
            return this.getSummary().compareTo(course.getSummary());
        return start;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cours cours = (Cours) o;
        return Objects.equals(summary, cours.summary) && Objects.equals(start, cours.start);
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
                        getSummary().contains("Fresque du Climat") ||
                        getSummary().contains("Réunion");
            }
            case 2 -> {
                return getSummary().contains("EMU") ||
                        getSummary().contains("EXAMEN") ||
                        getSummary().contains("Soutenance") ||
                        getSummary().contains("Réunion de rentrée") ||
                        getSummary().contains("Jeu d'accueil Ecole") ||
                        getSummary().contains("TER");
            }
            case 3 -> {
                return getSummary().contains("EIU") ||
                        getSummary().contains("Projet") ||
                        getSummary().contains("PGE") ||
                        getSummary().contains("Accessibilité") ||
                        getSummary().contains("Réunion de rentrée") ||
                        getSummary().contains("TOEIC");
            }
            default -> {
                return false;
            }
        }
    }

    public boolean isNotAccepted(int annee) {
        return !isAccepted(annee);
    }

    public boolean isInTheSameTime(Cours cours) {
        return cours.getStart().compareTo(this.getEnd()) < 0 && cours.getStart().compareTo(this.getStart()) > 0 ||
                cours.getEnd().compareTo(this.getEnd()) < 0 && cours.getEnd().compareTo(this.getStart()) > 0 ||
                this.getStart().compareTo(cours.getEnd()) < 0 && this.getStart().compareTo(cours.getStart()) > 0 ||
                this.getEnd().compareTo(cours.getEnd()) < 0 && this.getEnd().compareTo(cours.getStart()) > 0 ||
                cours.getStart().compareTo(this.getStart()) == 0 || cours.getEnd().compareTo(this.getEnd()) == 0;
    }

    public boolean isExam(GestionEdt gestionEdt) {
        int annee = 1;
        if (gestionEdt instanceof GestionEdt2A) {
            annee = 2;
        } else if (gestionEdt instanceof GestionEdt3A){
            annee = 3;
        }
        switch (annee) {
            case 1, 3 -> {
                return getSummary().contains("EXAMEN");
            }
            case 2 -> {
                return getSummary().contains("***") || getSummary().contains("EXAMEN");
            }
        }
        return false;
    }
}
