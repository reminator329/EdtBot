package reminator.EdtBot.Commands.genericEvent.commandEvent;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import reminator.EdtBot.Commands.genericEvent.action.CommandAction;
import reminator.EdtBot.Commands.genericEvent.action.SlashCommandAction;
import reminator.EdtBot.Commands.genericEvent.guildEvent.GuildEvent;
import reminator.EdtBot.Commands.genericEvent.guildEvent.GuildReceivedEvent;
import reminator.EdtBot.Commands.genericEvent.guildEvent.GuildUpdateEvent;

public class SlashCommandEvent implements CommandEvent {

    private final net.dv8tion.jda.api.events.interaction.SlashCommandEvent event;

    public SlashCommandEvent(net.dv8tion.jda.api.events.interaction.SlashCommandEvent event) {
        this.event = event;
    }
    @Override
    public CommandAction<InteractionHook> reply(String message) {
        return new SlashCommandAction(event.reply(message));
    }

    @Override
    public Member getMember() {
        return event.getMember();
    }

    @Override
    public Guild getGuild() {
        return event.getGuild();
    }
}
