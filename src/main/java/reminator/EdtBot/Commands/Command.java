package reminator.EdtBot.Commands;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Objects;

public abstract class Command {

    private String prefix;
    private String label;
    private ArrayList<String> aliass = new ArrayList<>();
    private MessageEmbed help;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public MessageEmbed getHelp() {
        return help;
    }

    public void setHelp(MessageEmbed help) {
        this.help = help;
    }

    public abstract MessageEmbed setHelp();

    public abstract void executerCommande(GuildMessageReceivedEvent event);

    public boolean isAlias(String alias) {
        return this.aliass.contains(alias);
    }

    protected void addAlias (String alias) {
        aliass.add(alias);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return prefix.equals(command.prefix) &&
                label.equals(command.label) &&
                Objects.equals(help, command.help);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prefix, label, help);
    }
}
