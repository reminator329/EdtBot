package reminator.EdtBot.edt;

import reminator.EdtBot.edt.gestionEdt.GestionEdt;
import reminator.EdtBot.edt.gestionEdt.GestionEdt1A;
import reminator.EdtBot.edt.gestionEdt.GestionEdt2A;
import reminator.EdtBot.edt.gestionEdt.GestionEdt3A;

import java.util.Comparator;
import java.util.TreeSet;

public class Day extends TreeSet<Stack> {

    public void addCourse(Cours course, GestionEdt gestionEdt) {

        if (gestionEdt instanceof GestionEdt1A) {
            if (course.isNotAccepted(1)) return;
        } else if (gestionEdt instanceof GestionEdt2A) {
            if (course.isNotAccepted(2)) return;
        } else if (gestionEdt instanceof GestionEdt3A)
            if (course.isNotAccepted(3)) return;

        boolean find = false;
        for (Stack stack : this) {
            if (stack.stream().anyMatch(course::isInTheSameTime)) {
                find = true;
                course.setPosition(stack.last().getPosition() + 1);
                stack.add(course);
            }
        }
        if (!find) {
            Stack stack = new Stack();
            stack.add(course);
            this.add(stack);
        }
    }

    public void removeCourse(Cours course) {
        this.remove(course);
    }
}
