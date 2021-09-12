package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import reminator.EdtBot.Categories.Category;
import reminator.EdtBot.Categories.enums.Categories;
import reminator.EdtBot.Commands.argument.Argument;
import reminator.EdtBot.Commands.argument.Arguments;
import reminator.EdtBot.Commands.genericEvent.commandEvent.CommandEvent;

import java.util.ArrayList;
import java.util.List;

public class ResetRolesCommand implements Command {

    @Override
    public List<Argument<?>> getArguments() {
        return new ArrayList<>();
    }

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
    public void execute(CommandEvent event, User author, MessageChannel channel, Arguments arguments) {

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR) && !author.getName().equalsIgnoreCase("reminator392")) {
            channel.sendMessage("Tu n'as pas la permission pour faire cette commande.").queue();
            return;
        }

        event.getGuild().getMembers().forEach(member -> member.getRoles().forEach(role -> {
            if (isRole(role)) {
                event.getGuild().removeRoleFromMember(member, role).queue();
                channel.sendMessage("Role " + role.getAsMention() + " supprimé pour " + member.getAsMention()).queue();
            }
        }));
    }

    private boolean isRole(Role r) {
        String name = r.getName();
        return name.equalsIgnoreCase("SRI") ||
                name.equalsIgnoreCase("SRI - 1A") ||
                name.equalsIgnoreCase("SRI - 2A") ||
                name.equalsIgnoreCase("SRI - 3A") ||

                name.equalsIgnoreCase("STRI") ||
                name.equalsIgnoreCase("STRI - 1A") ||
                name.equalsIgnoreCase("STRI - 2A") ||
                name.equalsIgnoreCase("STRI - 3A") ||

                name.equalsIgnoreCase("GCGEO") ||
                name.equalsIgnoreCase("GCGEO - 1A") ||
                name.equalsIgnoreCase("GCGEO - 2A") ||
                name.equalsIgnoreCase("GCGEO - 3A") ||

                name.equalsIgnoreCase("Etudiant") ||
                name.equalsIgnoreCase("Resp. Disc.");
    }
}
