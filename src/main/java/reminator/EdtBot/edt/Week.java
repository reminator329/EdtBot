package reminator.EdtBot.edt;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;

public class Week implements Iterable<Day> {

    private final Day[] days = new Day[5];

    public Week() {
        for (int i = 0; i < 5; i++) days[i] = new Day(i);
    }

    public void setDay(int i, Day day) {
        days[i] = day;
    }

    public Day[] getDays() {
        return days;
    }

    @NotNull
    @Override
    public Iterator<Day> iterator() {
        return Arrays.stream(days).iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Week week = (Week) o;
        return Arrays.equals(days, week.days);
    }
}
