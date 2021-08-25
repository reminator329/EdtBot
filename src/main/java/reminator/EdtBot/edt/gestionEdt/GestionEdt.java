package reminator.EdtBot.edt.gestionEdt;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.json.JSONArray;
import reminator.EdtBot.bot.BotEmbed;
import reminator.EdtBot.edt.*;
import reminator.EdtBot.utils.CoursParser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public abstract class GestionEdt {

    protected ArrayList<Cours> nextCourses = new ArrayList<>();
    protected ArrayList<Cours> courses;
    protected String csv;

    public GestionEdt() {
        courses = new ArrayList<>();
    }

    public ArrayList<Cours> getNextCourse() {
        courses.clear();
        updateCsv();
        Date date = new Date();
        updateEdt(date.getTime(), date.getTime() + 1000L * 3600 * 24 * 7 * 20);
        nextCourses.clear();
        return getNextCourses();
    }

    protected abstract ArrayList<Cours> getNextCourses();

    public Week getNextWeek() {

        Calendar cal = Calendar.getInstance();
        Date nextCourse = getNextCourse().get(0).getStart();
        cal.setTime(nextCourse);
        System.out.println(cal.getTime());

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        Date min = cal.getTime();

        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        Date max = cal.getTime();

        System.out.println(min);
        System.out.println(max);

        updateEdt(min.getTime(), max.getTime());

        Week week = new Week();
        for (int i : new int[]{Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY}) {
            Day day = new Day();
            courses.stream().filter(c -> {
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(c.getStart());
                return i == cal2.get(Calendar.DAY_OF_WEEK);
            }).forEach(c -> day.addCourse(c, this));

            week.addDay(i - 2, day);
        }

        return week;
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

    public void printCourse(Cours cours, MessageChannel channel) {

        EmbedBuilder builder = BotEmbed.COURSE.getBuilder(cours);

        String groupe = cours.getGroupe();
        if ("0".equals(groupe)) {
            builder.setTitle("Prochain cours");
        } else {
            builder.setTitle("Prochain cours, groupe " + groupe);
        }
        builder.appendDescription(cours.getSummary());

        channel.sendMessage(builder.build()).queue();
    }

    public void printWeek(Week week, MessageChannel channel) {

        System.out.println(week);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat jour = new SimpleDateFormat("EEEEEEEEEEEEEEEE dd MMMMMMMMMM", Locale.FRANCE);
        SimpleDateFormat heure = new SimpleDateFormat("H:mm", Locale.FRANCE);

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

                channel.sendMessage(builder.build()).queue();
            }
        }
        printImageWeek(week, channel);
    }

    protected void printImageWeek(Week week, MessageChannel channel) {

        File file;

        BufferedImage image;
        Graphics2D g;
        try {
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
            int courseHorizontalOffset = 5;

            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            g = (Graphics2D) bufferedImage.getGraphics();
            g.setColor(new Color(0x37474f));
            g.drawRect(0, 0, width, height);
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
            try {
                cal.setTime(anyCourse.get().getStart());
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                int firstDay = dayOfMonth - (dayOfWeek - 2);

                int monthNumber = cal.get(Calendar.MONTH);
                String mois = switch (monthNumber) {
                    case 0 -> "Janvier";
                    case 1 -> "Février";
                    case 2 -> "Mars";
                    case 3 -> "Avril";
                    case 4 -> "Mai";
                    case 5 -> "Juin";
                    case 6 -> "Juillet";
                    case 7 -> "Août";
                    case 8 -> "Septembre";
                    case 9 -> "Octobre";
                    case 10 -> "Novembre";
                    case 11 -> "Décembre";
                    default -> throw new IllegalStateException("Unexpected value: " + monthNumber);
                };
                g.setColor(Color.WHITE);
                g.drawString(mois, hourWidth / 2 - g.getFontMetrics().stringWidth(mois), dayHeight / 2 + g.getFontMetrics().getHeight() / 2);
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
                    text += " " + (firstDay + i);
                    g.drawString(text, hourWidth + i * coursesWidth / 5 + coursesWidth / 10 - g.getFontMetrics().stringWidth(text) / 2, dayHeight / 2 + g.getFontMetrics().getHeight() / 2);
                }

                // Courses
                //g.fillRoundRect(hourWidth + courseHorizontalOffset, dayHeight + hourHeight, dayWidth - 2 * courseHorizontalOffset, 10 * hourHeight, 20, 20);

                for (Day day : week) {
                    if (day.size() != 0) {
                        for (Stack stack : day) {
                            int nbCourses = stack.size();
                            for (Cours cours : stack) {
                                int position = cours.getPosition();
                                if (cours.getSummary().contains("EXAMEN"))
                                    g.setColor(new Color(0xe91e63));
                                else
                                    g.setColor(new Color(0x3f51b5));
                                cal.setTime(cours.getStart());
                                // Lundi = 2 - 2 = 0
                                int d = cal.get(Calendar.DAY_OF_WEEK) - 2;
                                // Notre emploi du temps ne commence pas avant 7 heures
                                int courseStartHour = cal.get(Calendar.HOUR_OF_DAY) - 7;
                                int courseStartMinute = cal.get(Calendar.MINUTE);
                                cal.setTime(cours.getEnd());
                                int courseEndHour = cal.get(Calendar.HOUR_OF_DAY) - 7;
                                int courseEndMinute = cal.get(Calendar.MINUTE);

                                g.fillRoundRect(
                                        hourWidth + courseHorizontalOffset + dayWidth * d + position * dayWidth / nbCourses,
                                        dayHeight + hourHeight * courseStartHour + hourHeight * courseStartMinute / 60,
                                        dayWidth / nbCourses - 2 * courseHorizontalOffset,
                                        hourHeight * (courseEndHour - courseStartHour) + hourHeight * (courseEndMinute - courseStartMinute) / 60 - 1,
                                        15,
                                        15);
                            }
                        }
                    }
                }

                g.dispose();
                ImageIO.write(bufferedImage, "png", new File("/EdtBot/images/test.png"));
                file = new File("/EdtBot/images/test.png");
                channel.sendMessage(" ").addFile(file).queue();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            channel.sendMessage(e.getMessage()).queue();
        }
    }
}
