package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import reminator.EdtBot.Categories.Category;
import reminator.EdtBot.Categories.enums.Categories;
import reminator.EdtBot.bot.BotEmbed;
import reminator.EdtBot.bot.EdtBot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
        File file = new File("/EdtBot/images/pioche.png");

        BufferedImage image;
        try {

            image = ImageIO.read(file);
            Graphics g = image.getGraphics();
            g.drawImage(image, 50, 50, 50, 50, null);
            g.setColor(Color.BLACK);
            g.drawString("C'est quoi ce truc ?????", 0, 10);
            g.setColor(Color.MAGENTA);
            g.drawString("Ceci n'est pas une pioche", 20, 20);
            g.dispose();
            ImageIO.write(image, "png", new File("/EdtBot/images/pioche2.png"));

            file = new File("/EdtBot/images/pioche2.png");
            channel.sendMessage(" ").addFile(file).queue();

            int width = 1024;
            int height = 512;
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            g = bufferedImage.getGraphics();
            g.setColor(Color.GREEN);
            g.drawRect(20, 20, 200, 200);
            g.setColor(Color.MAGENTA);
            g.drawString("Parfait", 50, 50);

            g.setColor(Color.RED);
            for (int i = 1 ; i < 5 ; i++) {
                g.drawLine(width*i/5, 0, width*i/5, height);
            }
            g.dispose();
            ImageIO.write(bufferedImage, "png", new File("/EdtBot/images/test.png"));
            file = new File("/EdtBot/images/test.png");
            channel.sendMessage(" ").addFile(file).queue();

        } catch (IOException ex) {
            ex.printStackTrace();
        }


        e.getGuild().getMembers().forEach(member -> {
            member.getRoles().forEach(role -> {
                if (isRole(role)) {
                    e.getGuild().removeRoleFromMember(member, role).queue();
                    channel.sendMessage("Role " + role.getAsMention() + " supprimé pour " + member.getAsMention()).queue();
                }
            });
        });
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
