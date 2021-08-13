package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import reminator.EdtBot.Categories.Category;
import reminator.EdtBot.Categories.enums.Categories;
import reminator.EdtBot.bot.BotEmbed;
import reminator.EdtBot.bot.EdtBot;

import java.util.List;

public class ResetRolesCommand implements Command {

    @Override
    public Category getCategory() {
        return Categories.ADMIN.getCategory();
    }

    @Override
    public String getLabel() {
        return "reset-roles";
    }

    @Override
    public String getPrefix() {
        return "admin!";
    }

    @Override
    public String[] getAlliass() {
        return new String[]{"reset", "r-r"};
    }

    @Override
    public String getDescription() {
        return "Supprime les roles de chaque étudiant.";
    }

    @Override
    public void execute(GenericGuildMessageEvent e, User author, MessageChannel channel, List<String> args) {

        if (e instanceof GuildMessageReceivedEvent event) {
            if (event.getMember() == null) return;
            if (!event.getMember().hasPermission(Permission.ADMINISTRATOR) && !event.getAuthor().getName().equalsIgnoreCase("reminator392")) {
                channel.sendMessage("Tu n'as pas la permission pour faire cette commande.").queue();
                return;
            }
        } else if (e instanceof GuildMessageUpdateEvent event){
            if (event.getMember() == null) return;
            if (!event.getMember().hasPermission(Permission.ADMINISTRATOR) && !event.getAuthor().getName().equalsIgnoreCase("reminator392")) {
                channel.sendMessage("Tu n'as pas la permission pour faire cette commande.").queue();
                return;
            }
        }

        e.getGuild().getMembers().forEach(member -> {
            member.getRoles().forEach(role -> {
                if (role.getName().equalsIgnoreCase("SRI")) {
                    e.getGuild().removeRoleFromMember(member, role).queue();
                    channel.sendMessage("Role " + role + " supprimé pour " + member.getAsMention()).queue();
                }
            });
        });

    }
}
