package reminator.EdtBot.bot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import reminator.EdtBot.Categories.AutresCategorie;
import reminator.EdtBot.Categories.Categorie;
import reminator.EdtBot.Categories.EdtCategorie;
import reminator.EdtBot.Commands.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.TimeZone;

public class Controller extends ListenerAdapter {

    private final ArrayList<Command> commands = new ArrayList<>();
    private final ProchainCoursCommand prochainCoursCommand;
    private final EcouteEdtCommand ecouteEdtCommand;
    private final PingCommand pingCommand;
    private final HelpCommand helpCommand;

    private final ArrayList<Categorie> categories = new ArrayList<>();
    private final EdtCategorie edtCategorie = new EdtCategorie();
    private final AutresCategorie autresCategorie = new AutresCategorie();

    public Controller() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));

        // Catégories
        categories.add(edtCategorie);
        categories.add(autresCategorie);

        // Commandes
        prochainCoursCommand = new ProchainCoursCommand();
        ecouteEdtCommand = new EcouteEdtCommand();
        pingCommand = new PingCommand();
        helpCommand = new HelpCommand(this);

        // Ajout de la commande dans la liste
        commands.add(pingCommand);
        commands.add(prochainCoursCommand);
        commands.add(ecouteEdtCommand);
        commands.add(helpCommand);

        // Ajout de la commande dans la catégorie
        edtCategorie.addCommand(prochainCoursCommand);
        edtCategorie.addCommand(ecouteEdtCommand);

        autresCategorie.addCommand(pingCommand);
        autresCategorie.addCommand(helpCommand);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.isFromGuild()) return;

        event.getAuthor().openPrivateChannel()
                .flatMap(channel -> channel.sendMessage("3"))
                .delay(Duration.ofSeconds(1))
                .flatMap(channel -> channel.editMessage("2"))
                .delay(Duration.ofSeconds(1))
                .flatMap(channel -> channel.editMessage("1"))
                .delay(Duration.ofSeconds(1))
                .flatMap(Message::delete)
                .queue();
    }

    @Override
    public void onGuildMessageUpdate(@NotNull GuildMessageUpdateEvent event) {
        System.out.println("test");
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        for (Command c : commands) {
            String prefixLabel = c.getPrefix() + c.getLabel();
            String[] test = event.getMessage().getContentRaw().split(c.getPrefix());
            if (prefixLabel.equalsIgnoreCase(args[0]) || test.length > 1 && c.isAlias(test[1])) {
                c.executerCommande(event);
            }
        }
    }

    public ArrayList<Categorie> getCategories() {
        return new ArrayList<>(this.categories);
    }

    public ArrayList<Command> getCommands() {
        return new ArrayList<>(this.commands);
    }
}