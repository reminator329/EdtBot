package reminator.EdtBot.utils;

import net.dv8tion.jda.api.entities.MessageChannel;
import org.json.JSONObject;
import reminator.EdtBot.bot.EdtBot;
import reminator.EdtBot.edt.*;

import java.io.*;
import java.util.*;

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
        JSONObject json = new JSONObject(content.toString());
        json.put(idChannel, annee);

        try {
            bw = new BufferedWriter(new FileWriter(fileEcouteurs.getAbsoluteFile()));
            bw.write(json.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private Timer lancerTimer(MessageChannel channel, GestionEdt gestionEdt, boolean start) {

        Timer timer = new Timer();
        final ArrayList<Cours>[] cours = new ArrayList[]{new ArrayList<Cours>()};
        final ArrayList<Cours>[] pCours = new ArrayList[]{new ArrayList<Cours>()};
        final boolean[] s = {start};
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                pCours[0].clear();
                pCours[0].addAll(gestionEdt.getNextCourse());
                if (cours[0].size() == 0 || !cours[0].get(0).getSummary().equals(pCours[0].get(0).getSummary())) {
                    cours[0].clear();
                    cours[0].addAll(pCours[0]);
                    for (Cours c : cours[0]) {
                            gestionEdt.printCourse(c, channel);
                    }
                }
            }
        }, 0, 1000 * 60/*500 * 3600*/);
        return timer;
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
        json.remove(String.valueOf(idChannel));

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
            System.out.println(s);
            System.out.println(EdtBot.API.getUserById(368733622246834188L));
            System.out.println("OUI " + EdtBot.API.getTextChannelById(s));
            ecoutes.put(s, lancerTimer(EdtBot.API.getTextChannelById(s), switch (json.getInt(s)) {
                case 1 -> new GestionEdt1A();
                case 2 -> new GestionEdt2A();
                case 3 -> new GestionEdt3A();
                default -> throw new IllegalStateException("Unexpected value: " + json.getInt(s));
            }, true));
        });
    }
}
