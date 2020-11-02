package reminator.EdtBot.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class EdtBot {

    public static String prefix = "edt!";
    public static String token;

    public static void main(String[] arguments) throws Exception {
        token = arguments[0];
        JDA api = JDABuilder.create(token, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES).enableCache(CacheFlag.ACTIVITY).build();
        api.addEventListener(new Controller());
        api.getPresence().setPresence(OnlineStatus.ONLINE, Activity.listening("edt!help"));
    }
}
