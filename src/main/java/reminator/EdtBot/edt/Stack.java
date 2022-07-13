package reminator.EdtBot.edt;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.TreeSet;

public class Stack extends TreeSet<Cours> implements Comparable<Stack> {

    public Stack() {
        super((o1, o2) -> {
            int start = o1.getStart().compareTo(o2.getStart());
            if (start == 0)
                return o1.getPosition() - o2.getPosition();
            return start;
        });
    }

    @Override
    public int compareTo(@NotNull Stack o) {
        return this.first().compareTo(o.first());
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
