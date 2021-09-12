package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import reminator.EdtBot.Categories.Category;
import reminator.EdtBot.Categories.enums.Categories;
import reminator.EdtBot.Commands.argument.Argument;
import reminator.EdtBot.Commands.argument.Arguments;
import reminator.EdtBot.Commands.argument.OptionalArgument;
import reminator.EdtBot.Commands.genericEvent.commandEvent.CommandEvent;
import reminator.EdtBot.Commands.genericEvent.commandEvent.LegacyCommandEvent;
import reminator.EdtBot.bot.BotEmbed;

import java.util.List;
import java.util.Optional;

public class PingCommand implements Command {
    private final Argument<Optional<Boolean>> ephemeral = new OptionalArgument<>(Boolean.class, "ephemeral", "Mettre à true pour que personne de voit la réponse.");

    @Override
    public List<Argument<?>> getArguments() {
        return List.of(ephemeral);
    }

    @Override
    public Category getCategory() {
        return Categories.OTHER.getCategory();
    }

    @Override
    public String getLabel() {
        return "ping";
    }

    @Override
    public String[] getAlliass() {
        return new String[]{"p"};
    }

    @Override
    public String getDescription() {
        return "Répond pong et affiche la musique écoutée actuellement sur spotify.";
    }

    @Override
    public void execute(CommandEvent event, User author, MessageChannel channel, Arguments arguments) {

        Optional<Boolean> optEphemeral = arguments.get(ephemeral);
        boolean ephemeral;
        ephemeral = optEphemeral.orElse(false);

        long time = System.currentTimeMillis();
        event.reply("Pong!").setEphemeral(ephemeral)
                .thenEdit("Pong: " +  (System.currentTimeMillis() - time) + "ms");
    }
}
