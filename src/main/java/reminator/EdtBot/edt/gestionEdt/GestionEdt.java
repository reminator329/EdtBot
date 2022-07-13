package reminator.EdtBot.edt.gestionEdt;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.internal.utils.tuple.Pair;
import org.json.JSONArray;
import reminator.EdtBot.bot.BotEmbed;
import reminator.EdtBot.edt.Stack;
import reminator.EdtBot.edt.*;
import reminator.EdtBot.utils.CoursParser;
import reminator.EdtBot.utils.HttpServer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public abstract class GestionEdt {

    private final Map<String, BufferedImage> weekImages = new HashMap<>();
    protected ArrayList<Cours> nextCourses = new ArrayList<>();
    protected ArrayList<Cours> courses;
    protected String csv;
    protected Week thisWeek = new Week();

    public GestionEdt() {
        courses = new ArrayList<>();
    }

    public ArrayList<Cours> getNextCourse() {
        courses.clear();
        updateCsv();
        Date date = new Date();
        updateEdt(date.getTime(), date.getTime() + 1000L * 3600 * 24 * 7 * 15);
        nextCourses.clear();
        return getNextCourses();
    }

    protected abstract ArrayList<Cours> getNextCourses();

    public Week getNextWeek() {

        Calendar cal = Calendar.getInstance();
        Date nextCourse = getNextCourse().get(0).getStart();
        cal.setTime(nextCourse);

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        Date min = cal.getTime();

        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        Date max = cal.getTime();

        updateEdt(min.getTime(), max.getTime());

        for (int i : new int[]{Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY}) {
            Day day = new Day(i - 2);
            courses.stream().filter(c -> {
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(c.getStart());
                return i == cal2.get(Calendar.DAY_OF_WEEK);
            }).forEach(c -> day.addCourse(c, this));

            thisWeek.setDay(i - 2, day);
        }

        return thisWeek;
    }

    public TypeCourse getTypeCours(Cours cours) {

        TypeCourse type = new TypeCourse();

        try {
            Date date = cours.getStart();

            String jour = new CoursParser().getJour(csv, date, cours);
            if (jour != null) {
                type = new CoursParser().getTypeCours(jour, date);
            }

        } catch (NullPointerException ignored) {
        }
        return type;
    }

    protected void updateEdt(long min, long max) {
        courses.clear();
    }

    protected abstract void updateCsv();

    protected void ajoutCourss(JSONArray courss, String groupe) {

        for (int i = 0; i < courss.length(); i++) {
            Cours c = new CoursParser(groupe).parse(courss.getJSONObject(i));
            TypeCourse type = getTypeCours(c);
            c.setType(type);
            if (c.getStart() != null)
                courses.add(c);
        }
    }

    public CompletableFuture<Message> printCourse(Cours cours, MessageChannel channel) {

        EmbedBuilder builder = BotEmbed.COURSE.getBuilder(cours);

        String groupe = cours.getGroupe();
        if ("0".equals(groupe)) {
            builder.setTitle("Prochain cours");
        } else {
            builder.setTitle("Prochain cours, groupe " + groupe);
        }
        builder.appendDescription(cours.getSummary());

        return channel.sendMessageEmbeds(builder.build()).submit();
    }

    public Pair<CompletableFuture<Message>, CompletableFuture<Message>[]> printWeek(Week week, MessageChannel channel) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat jour = new SimpleDateFormat("EEEEEEEEEEEEEEEE dd MMMMMMMMMM", Locale.FRANCE);
        SimpleDateFormat heure = new SimpleDateFormat("H:mm", Locale.FRANCE);

        CompletableFuture<Message>[] jours = new CompletableFuture[5];
        for (Day day : week) {
            if (day.size() != 0) {
                EmbedBuilder builder = BotEmbed.BASE.getBuilder(null);
                cal.setTime(day.stream().findAny().get().stream().findAny().get().getStart());
                builder.setTitle(jour.format(cal.getTime()));
                builder.setColor(Color.GREEN);

                builder.setDescription(day.stream().map(
                        s -> s.stream().map(
                                c -> String.format("**%s - %s** %s", heure.format(c.getStart()), heure.format(c.getEnd()), c.getSummary().replace("*", ""))
                        ).collect(Collectors.joining("\n"))
                ).collect(Collectors.joining("\n")));

                jours[day.getDay()] = channel.sendMessageEmbeds(builder.build()).submit();
            }
        }
        return Pair.of(printImageWeek(week, channel), jours);
    }

    protected CompletableFuture<Message> printImageWeek(Week week, MessageChannel channel) {

        Graphics2D g;
        int width = 1024;
        int height = 512;

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        g = (Graphics2D) bufferedImage.getGraphics();
        drawCourses(g, week);
        g.dispose();
        weekImages.put(channel.getId(), bufferedImage);

        BufferedImage b = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
        Graphics2D g2 = (Graphics2D) b.getGraphics();
        g2.drawImage(bufferedImage, 0, 0, null);
        drawLine(g);

        g2.dispose();
        //ImageIO.write(bufferedImage, "png", new File("week_" + channel.getId() + "_2.png"));
        //file = new File("week_" + channel.getId() + "_2.png");
        return channel.sendMessage(HttpServer.INSTANCE.getWeekUrl(channel.getId(), b)).submit();
    }

    public void updateImageWeek(MessageChannel channel, String idWeek) {
        CompletableFuture<Message> week = channel.retrieveMessageById(idWeek).submit();
        week.thenAcceptAsync(message -> {

            BufferedImage b = weekImages.get(channel.getId());
            if (b == null) {
                int width = 1024;
                int height = 512;
                b = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
                drawCourses((Graphics2D) b.getGraphics(), this.getNextWeek());
            }
            BufferedImage b2 = new BufferedImage(b.getWidth(), b.getHeight(), b.getType());
            Graphics2D g2 = (Graphics2D) b2.getGraphics();
            g2.drawImage(b, 0, 0, null);
            drawLine(g2);

            g2.dispose();
            message.editMessage(HttpServer.INSTANCE.getWeekUrl(channel.getId(), b2)).queue();
        });
    }

    private void drawCourses(Graphics2D g, Week week) {
        int startHour = 7;
        int endHour = 20;
        int nbHours = endHour - startHour;
        int width = 1024;
        int height = 512;
        int fontHeight = 10;
        int hourWidth = fontHeight * 6;
        int dayHeight = fontHeight * 2;
        int coursesWidth = (width - hourWidth);
        int dayWidth = coursesWidth / 5;
        int coursesHeight = (height - dayHeight);
        int hourHeight = coursesHeight / 13;

        int dayLineOffset = 15;
        int hourLineOffset = 15;
        int textOffset = 5;
        int courseHorizontalOffset = 3;
        int shadowWidth = 5;

        g.setColor(new Color(0x37474f));
        g.fillRect(0, 0, width, height);
        g.setRenderingHints(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
        g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));


        // Lines
        g.setColor(Color.GRAY);
        g.drawLine(hourWidth, 0, hourWidth, height);
        g.drawLine(0, dayHeight, width, dayHeight);

        for (int i = 1; i <= 5; i++) {
            int startX = coursesWidth * i / 5 + hourWidth;
            g.drawLine(startX, dayHeight - dayLineOffset, startX, height);
        }

        for (int i = 1; i <= nbHours; i++) {
            int startY = hourHeight * i + dayHeight;
            g.drawLine(hourWidth - hourLineOffset, startY, width, startY);

            int hour = startHour + i;
            g.drawString("" + hour + "h", textOffset, dayHeight + i * hourHeight + textOffset);
        }

        // Date
        Calendar cal = Calendar.getInstance();
        AtomicReference<Cours> anyCourse = new AtomicReference<>();
        week.forEach(day -> {
            if (day.size() > 0) {
                anyCourse.set(day.stream().findAny().get().stream().findAny().get());
            }
        });
        cal.setTime(anyCourse.get().getStart());
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        g.setColor(Color.WHITE);
        //g.drawString(mois, hourWidth / 2 - g.getFontMetrics().stringWidth(mois), dayHeight / 2 + g.getFontMetrics().getHeight() / 2);
        for (int i = 0; i < 5; i++) {
            String text =
                    switch (i) {
                        case 0 -> "Lundi";
                        case 1 -> "Mardi";
                        case 2 -> "Mercredi";
                        case 3 -> "Jeudi";
                        case 4 -> "Vendredi";
                        default -> throw new IllegalStateException("Unexpected value: " + i);
                    };


            int monthNumber = cal.get(Calendar.MONTH) + 1;
            String textMouth = monthNumber < 10 ? "0" + monthNumber : "" + monthNumber;

            int dayNumber = cal.get(Calendar.DAY_OF_MONTH);
            String textDay = dayNumber < 10 ? "0" + dayNumber : "" + dayNumber;

            text += " " + textDay + "/" + textMouth;
            g.drawString(text, hourWidth + i * coursesWidth / 5 + coursesWidth / 10 - g.getFontMetrics().stringWidth(text) / 2, dayHeight / 2 + g.getFontMetrics().getHeight() / 2);
            cal.add(Calendar.DATE, 1);
        }

        // Courses
        //g.fillRoundRect(hourWidth + courseHorizontalOffset, dayHeight + hourHeight, dayWidth - 2 * courseHorizontalOffset, 10 * hourHeight, 20, 20);

        for (Day day : week) {
            if (day.size() != 0) {
                for (Stack stack : day) {
                    int nbCourses = stack.size();
                    for (Cours cours : stack) {
                        int position = cours.getPosition();
                        cal.setTime(cours.getStart());
                        // Lundi = 2 - 2 = 0
                        int d = cal.get(Calendar.DAY_OF_WEEK) - 2;
                        // Notre emploi du temps ne commence pas avant 7 heures
                        int courseStartHour = cal.get(Calendar.HOUR_OF_DAY) - 7;
                        int courseStartMinute = cal.get(Calendar.MINUTE);
                        cal.setTime(cours.getEnd());
                        int courseEndHour = cal.get(Calendar.HOUR_OF_DAY) - 7;
                        int courseEndMinute = cal.get(Calendar.MINUTE);

                        int x = hourWidth + courseHorizontalOffset + (dayWidth + 1) * d + position * dayWidth / nbCourses;
                        int y = dayHeight + hourHeight * courseStartHour + hourHeight * courseStartMinute / 60;
                        int widthRect = dayWidth / nbCourses - 2 * courseHorizontalOffset - shadowWidth;
                        int heightRect = hourHeight * (courseEndHour - courseStartHour) + hourHeight * (courseEndMinute - courseStartMinute) / 60 - 5;

                        if (cours.isExam(this))
                            g.setColor(new Color(0xa00037));
                        else
                            g.setColor(new Color(0x004ba0));
                        g.fillRoundRect(
                                x,
                                y,
                                widthRect + shadowWidth,
                                heightRect + shadowWidth,
                                15,
                                15);

                        if (cours.isExam(this))
                            g.setColor(new Color(0xd81b60));
                        else
                            g.setColor(new Color(0x1976d2));
                        g.fillRoundRect(
                                x,
                                y,
                                widthRect,
                                heightRect,
                                15,
                                15);
                    }
                }
            }
        }
    }

    private void drawLine(Graphics2D g) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int today = cal.get(Calendar.DAY_OF_WEEK) - 2;

        if (today > 4) return;

        int hour = cal.get(Calendar.HOUR_OF_DAY) - 7;
        int minute = cal.get(Calendar.MINUTE);

        int startHour = 7;
        int endHour = 20;
        int nbHours = endHour - startHour;
        int width = 1024;
        int height = 512;
        int fontHeight = 10;
        int hourWidth = fontHeight * 6;
        int dayHeight = fontHeight * 2;
        int coursesWidth = (width - hourWidth);
        int dayWidth = coursesWidth / 5;
        int coursesHeight = (height - dayHeight);
        int hourHeight = coursesHeight / 13;

        int x = hourWidth + today * (dayWidth + 1) + 1;
        int y;
        if (hour < 0) {
            y = 1;
        } else if (hour > 12) {
            y = height - 1;
        } else {
            y = dayHeight + hourHeight * hour + hourHeight * minute / 60;
        }

        g.setColor(Color.ORANGE);
        g.setStroke(new BasicStroke(5));
        g.drawLine(
                x,
                y,
                x + dayWidth - 3,
                y
        );
    }

    public void updateWeek(MessageChannel channel, String[] idDays) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat jour = new SimpleDateFormat("EEEEEEEEEEEEEEEE dd MMMMMMMMMM", Locale.FRANCE);
        SimpleDateFormat heure = new SimpleDateFormat("H:mm", Locale.FRANCE);
        Day[] days = this.thisWeek.getDays();

        for (int i = 0; i < idDays.length; i++) {
            if (idDays[i] == null) continue;
            CompletableFuture<Message> idDay = channel.retrieveMessageById(idDays[i]).submit();
            int finalI = i;
            idDay.thenAcceptAsync(message -> {
                EmbedBuilder builder = BotEmbed.BASE.getBuilder(null);
                Day day = days[finalI];

                cal.setTime(day.stream().findAny().get().stream().findAny().get().getStart());
                builder.setTitle(jour.format(cal.getTime()));
                builder.setColor(Color.GREEN);

                builder.setDescription(day.stream().map(
                        s -> s.stream().map(
                                c -> String.format("**%s - %s** %s", heure.format(c.getStart()), heure.format(c.getEnd()), c.getSummary().replace("*", ""))
                        ).collect(Collectors.joining("\n"))
                ).collect(Collectors.joining("\n")));

                message.editMessageEmbeds(builder.build()).queue();
            });
        }
    }
}
