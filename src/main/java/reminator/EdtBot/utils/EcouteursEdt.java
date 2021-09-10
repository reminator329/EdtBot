package reminator.EdtBot.utils;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import reminator.EdtBot.bot.EdtBot;
import reminator.EdtBot.edt.Cours;
import reminator.EdtBot.edt.Week;
import reminator.EdtBot.edt.gestionEdt.GestionEdt;
import reminator.EdtBot.edt.gestionEdt.GestionEdt1A;
import reminator.EdtBot.edt.gestionEdt.GestionEdt2A;
import reminator.EdtBot.edt.gestionEdt.GestionEdt3A;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class EcouteursEdt {

    private static final Map<String, Timer> ecoutes = new TreeMap<>();
    private final String nomFile = "ecouteur_edt.txt";
    private final File fileEcouteurs = new File(nomFile);
    private BufferedReader br;
    private BufferedWriter bw;

    public void ajoutEcouteur(String idChannel, int annee) {
        ecoutes.put(idChannel, lancerTimer(EdtBot.API.getTextChannelById(idChannel), switch (annee) {
            case 1 -> new GestionEdt1A();
            case 2 -> new GestionEdt2A();
            case 3 -> new GestionEdt3A();
            default -> throw new IllegalStateException("Unexpected value: " + annee);
        }, false));

        try {
            br = new BufferedReader(new FileReader(nomFile));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        StringBuilder content = new StringBuilder();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (content.toString().equals("")) {
            content.append("{}");
        }
        JSONObject jsonChannels = new JSONObject(content.toString());

        JSONObject jsonChannel = new JSONObject();
        jsonChannel.put("annee", annee);

        jsonChannels.put(idChannel, jsonChannel);

        try {
            bw = new BufferedWriter(new FileWriter(fileEcouteurs.getAbsoluteFile()));
            bw.write(jsonChannels.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Timer lancerTimer(MessageChannel channel, GestionEdt gestionEdt, boolean start) {

        Timer timer = new Timer();
        final ArrayList<Cours>[] cours = new ArrayList[]{new ArrayList<Cours>()};
        final ArrayList<Cours>[] pCours = new ArrayList[]{new ArrayList<Cours>()};
        final boolean[] s = {start};

        Calendar cal = Calendar.getInstance();
        final int[] currentWeek = {-1};

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                pCours[0].clear();
                pCours[0].addAll(gestionEdt.getNextCourse());
                System.out.println("test");
                Week week = null;
                if (cours[0].size() == 0 || !cours[0].get(0).getSummary().equals(pCours[0].get(0).getSummary())) {
                    cours[0].clear();
                    cours[0].addAll(pCours[0]);
                    System.out.println("\n\n\n BONJOUR");
                    System.out.println(cours[0]);

                    cal.setTime(cours[0].get(0).getStart());

                    if (!s[0]) {
                        if (currentWeek[0] != cal.get(Calendar.WEEK_OF_YEAR)) {
                            week = gestionEdt.getNextWeek();
                            saveIdWeek(channel, gestionEdt.printWeek(week, channel));
                            currentWeek[0] = cal.get(Calendar.WEEK_OF_YEAR);
                        }

                        deleteLastCourses(channel);

                        for (Cours c : cours[0]) {
                            saveIdNextCourse(channel, gestionEdt.printCourse(c, channel));
                        }
                    } else {
                        currentWeek[0] = cal.get(Calendar.WEEK_OF_YEAR);
                        s[0] = false;
                    }
                }
                String idWeek = getIdWeek(channel);
                if (idWeek != null)
                    gestionEdt.updateWeek(channel, idWeek);
            }
        }, 0, 1000 * 60/*500 * 3600*/);
        return timer;
    }

    private String getIdWeek(MessageChannel channel) {

        try {
            br = new BufferedReader(new FileReader(nomFile));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder content = new StringBuilder();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (content.toString().equals("")) {
            content.append("{}");
        }
        JSONObject json = new JSONObject(content.toString());
        JSONObject jsonChannel = json.getJSONObject(channel.getId());
        String week;
        try {
            week = jsonChannel.getString("week");
        } catch (JSONException e) {
            week = null;
        }

        return week;
    }

    private void deleteLastCourses(MessageChannel channel) {

        try {
            br = new BufferedReader(new FileReader(nomFile));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        StringBuilder content = new StringBuilder();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (content.toString().equals("")) {
            content.append("{}");
        }
        JSONObject json = new JSONObject(content.toString());
        JSONObject jsonChannel = json.getJSONObject(channel.getId());
        JSONArray jsonNextCourses;

        try {
            jsonNextCourses = jsonChannel.getJSONArray("nextCourses");
        } catch (JSONException e) {
            return;
        }
        jsonNextCourses.forEach(o -> {
            String idMessage = (String) o;
            channel.deleteMessageById(idMessage).queue();
        });

        jsonNextCourses.clear();
        jsonChannel.put("nextCourses", jsonNextCourses);
        json.put(channel.getId(), jsonChannel);

        try {
            bw = new BufferedWriter(new FileWriter(fileEcouteurs.getAbsoluteFile()));
            bw.write(json.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveIdWeek(MessageChannel channel, CompletableFuture<Message> message) {
        message.thenAcceptAsync(m -> {
            System.out.println("testeeeeeeeeeee");
            try {
                br = new BufferedReader(new FileReader(nomFile));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            StringBuilder content = new StringBuilder();
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    content.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            if (content.toString().equals("")) {
                content.append("{}");
            }
            JSONObject json = new JSONObject(content.toString());
            JSONObject jsonChannel = json.getJSONObject(channel.getId());

            System.out.println("ENFINNNNN");
            jsonChannel.put("week", m.getId());
            json.put(channel.getId(), jsonChannel);
            System.out.println(json);

            try {
                bw = new BufferedWriter(new FileWriter(fileEcouteurs.getAbsoluteFile()));
                bw.write(json.toString());
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void saveIdNextCourse(MessageChannel channel, CompletableFuture<Message> message) {
        message.thenAcceptAsync(m -> {

            try {
                br = new BufferedReader(new FileReader(nomFile));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            StringBuilder content = new StringBuilder();
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    content.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            if (content.toString().equals("")) {
                content.append("{}");
            }
            JSONObject json = new JSONObject(content.toString());
            JSONObject jsonChannel = json.getJSONObject(channel.getId());
            JSONArray jsonNextCourses;

            try {
                jsonNextCourses = jsonChannel.getJSONArray("nextCourses");
            } catch (JSONException e) {
                jsonNextCourses = new JSONArray();
            }
            JSONArray finalJsonNextCourses = jsonNextCourses;
            JSONArray finalJsonNextCourses1 = jsonNextCourses;
            finalJsonNextCourses.put(m.getId());
            jsonChannel.put("nextCourses", finalJsonNextCourses1);
            json.put(channel.getId(), jsonChannel);

            try {
                bw = new BufferedWriter(new FileWriter(fileEcouteurs.getAbsoluteFile()));
                bw.write(json.toString());
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void supprimerEcouteur(String idChannel) {

        if (ecoutes.get(idChannel) == null) return;
        ecoutes.get(idChannel).cancel();
        ecoutes.get(idChannel).purge();
        ecoutes.remove(idChannel);

        try {
            br = new BufferedReader(new FileReader(nomFile));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        StringBuilder content = new StringBuilder();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (content.toString().equals("")) {
            content.append("{}");
        }
        JSONObject json = new JSONObject(content.toString());
        json.remove(idChannel);

        try {
            bw = new BufferedWriter(new FileWriter(fileEcouteurs.getAbsoluteFile()));
            bw.write(json.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {

        try {
            br = new BufferedReader(new FileReader(nomFile));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        StringBuilder content = new StringBuilder();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (content.toString().equals("")) {
            content.append("{}");
        }
        JSONObject json = new JSONObject(content.toString());

        json.keySet().forEach(s -> {
            JSONObject jsonChannel = json.getJSONObject(s);
            ecoutes.put(s, lancerTimer(EdtBot.API.getTextChannelById(s), switch (jsonChannel.getInt("annee")) {
                case 1 -> new GestionEdt1A();
                case 2 -> new GestionEdt2A();
                case 3 -> new GestionEdt3A();
                default -> throw new IllegalStateException("Unexpected value: " + jsonChannel.getInt("annee"));
            }, true));
        });
    }
}
