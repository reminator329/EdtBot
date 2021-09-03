package reminator.EdtBot.edt;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

public class Week implements Iterable<Day> {

    private final Day[] days = new Day[5];

    public Week() {
        for (int i = 0; i < 5; i++) days[i] = new Day();
    }

    public void addDay(int i, Day day) {
        days[i] = day;
    }

    @NotNull
    @Override
    public Iterator<Day> iterator() {
        return Arrays.stream(days).iterator();
    }
}
