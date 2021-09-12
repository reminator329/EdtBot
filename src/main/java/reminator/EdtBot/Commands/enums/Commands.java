package reminator.EdtBot.Commands.enums;

import reminator.EdtBot.Categories.Category;
import reminator.EdtBot.Commands.*;
import reminator.EdtBot.Commands.argument.Argument;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum Commands {

    PING(new PingCommand()),
    HELP(new HelpCommand()),
    NEXT_COURSE(new ProchainCoursCommand()),
    WEEK(new WeekCommand()),
    LISTEN_EDT(new EcouteEdtCommand()),
    RESET_ROLES(new ResetRolesCommand())
    ;

    private final Command command;

    Commands(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

    public static Map<Category, List<Command>> getCommandsGroupedByCategory() {
        return Arrays.stream(Commands.values()).map(Commands::getCommand).collect(Collectors.groupingBy(Command::getCategory));
    }

    public static Command getCommand(String label) {
        for (Commands commands : values()) {
            Command command = commands.getCommand();
            if (command.getLabel().equalsIgnoreCase(label) || command.isAlias(label)) {
                return command;
            }
        }
        return null;
    }
}
