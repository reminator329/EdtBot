package reminator.EdtBot.edt;

import org.jetbrains.annotations.NotNull;

import java.util.TreeSet;

public class Stack extends TreeSet<Cours> implements Comparable<Stack> {
    @Override
    public int compareTo(@NotNull Stack o) {
        return this.first().compareTo(o.first());
    }
}
