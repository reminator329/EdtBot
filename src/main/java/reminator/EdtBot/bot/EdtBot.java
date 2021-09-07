package reminator.EdtBot.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import reminator.EdtBot.edt.enums.Liens;
import reminator.EdtBot.utils.EcouteursEdt;
import reminator.EdtBot.utils.HttpServer;

import java.awt.*;

public class EdtBot {

    public static final String prefix = "edt!";
    public static final Color color = Color.RED;
    public static String token;
    public static JDA API;

    public static void main(String[] arguments) throws Exception {
        token = arguments[0];
        Liens.CSV_1A.setUrl(arguments[1]);
        JDA api = JDABuilder.create(token, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES).enableCache(CacheFlag.ACTIVITY).build();
        api.awaitReady();
        API = api;
        api.addEventListener(new Controller());
        api.getPresence().setPresence(OnlineStatus.ONLINE, Activity.listening("edt!help"));

        CommandListUpdateAction commands = api.updateCommands()
                .addCommands(
                new CommandData("ping", "Retourne pong")
                        .addOption(OptionType.BOOLEAN, "ephemeral", "Mettre à true pour que personne de voit la réponse.", false)
        );
        commands.queue();

        HttpServer.INSTANCE.start();
        new EcouteursEdt().start();
    }
}
