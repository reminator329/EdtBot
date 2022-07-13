package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import reminator.EdtBot.Categories.Category;
import reminator.EdtBot.Categories.enums.Categories;
import reminator.EdtBot.Commands.argument.Argument;
import reminator.EdtBot.Commands.argument.Arguments;
import reminator.EdtBot.Commands.genericEvent.commandEvent.CommandEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveRoleCommand implements Command {
    private final Argument<String> role = new Argument<>(String.class, "role", "Rôle à enlever pour tous les membres.");

    @Override
    public List<Argument<?>> getArguments() {
        return List.of(role);
    }

    @Override
    public Category getCategory() {
        return Categories.ADMIN.getCategory();
    }

    @Override
    public String getLabel() {
        return "remove-roles";
    }

    @Override
    public String getPrefix() {
        return "admin!";
    }

    @Override
    public String[] getAlliass() {
        return new String[]{"remove", "r-r"};
    }

    @Override
    public String getDescription() {
        return "Supprime le role donné pour chaque membre.";
    }

    @Override
    public void execute(CommandEvent event, User author, MessageChannel channel, Arguments arguments) {

        if (event.getMember() == null) {
            return;
        }
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR) && !event.getMember().getUser().getId().equals("368733622246834188")) {
            channel.sendMessage("Tu n'as pas la permission pour faire cette commande.").queue();
            return;
        }

        String roleString = arguments.get(this.role);

        Guild guild = event.getGuild();

        Pattern pattern = Pattern.compile("<@&([0-9]+)>");
        Matcher matcher = pattern.matcher(roleString);

        if (matcher.find()) {
            String idRole = matcher.group(1);
            Role role = guild.getRoleById(idRole);
            System.out.println(role);
            if (role == null) {
                channel.sendMessage("Le rôle n'existe pas.").queue();
                return;
            }

            guild.pruneMemberCache();
            List<Member> members = guild.getMembersWithRoles(role);
            System.out.println(members);

            members.forEach(m -> {
                guild.removeRoleFromMember(m, role).queue();
                channel.sendMessage("Le rôle " + role.getAsMention() + " a été retiré pour " + m.getAsMention() + " (" + m.getNickname() + ").").queue();
            });
        } else {
            channel.sendMessage("Ceci n'est pas un rôle valide.").queue();
        }
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
