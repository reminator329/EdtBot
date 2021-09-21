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
import reminator.EdtBot.Commands.Command;
import reminator.EdtBot.Commands.argument.Argument;
import reminator.EdtBot.Commands.enums.Commands;
import reminator.EdtBot.edt.enums.Liens;
import reminator.EdtBot.utils.EcouteursEdt;
import reminator.EdtBot.utils.HttpServer;

import java.awt.*;
import java.util.Arrays;

public class EdtBot {

    public static final String prefix = "edt!";
    public static final Color color = Color.RED;
    public static String token;
    public static JDA API;

    public static void main(String[] arguments) throws Exception {
        token = "NzY0OTM3NTQ1NzE1OTQxNDA2.X4Nhmg.gi3FdTGamG6TtTUrepJTzpU6ee4";
        Liens.CSV_1A.setUrl(arguments[1]);
        JDA api = JDABuilder.create(token, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES).enableCache(CacheFlag.ACTIVITY).build();
        api.awaitReady();
        API = api;
        api.addEventListener(new Controller());
        api.getPresence().setPresence(OnlineStatus.ONLINE, Activity.listening("edt!help"));

        CommandListUpdateAction commands = api.updateCommands();

        for (Command c : Arrays.stream(Commands.values()).map(Commands::getCommand).toList()) {
            CommandData data = new CommandData(c.getLabel(), c.getShortDescription());

            for (Argument<?> argument : c.getArguments()) {
                OptionType type;
                Class<?> argumentType = argument.getType();

                if (Boolean.class.equals(argumentType)) {
                    type = OptionType.BOOLEAN;
                } else if (String.class.equals(argumentType)) {
                    type = OptionType.STRING;
                } else if (Integer.class.equals(argumentType)) {
                    type = OptionType.INTEGER;
                } else {
                    throw new IllegalStateException("Unexpected value: " + argument.getType());
                }
                data.addOption(type, argument.getName(), argument.getDescription(), !argument.isOptional());
            }

            commands.addCommands(data).queue();
        }
        commands.queue();

        HttpServer.INSTANCE.start();
        new EcouteursEdt().start();
    }
}
