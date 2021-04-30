package reminator.EdtBot.Commands.enums;

import reminator.EdtBot.Commands.*;

public enum Commands {
    PING(new PingCommand()),
    HELP(new HelpCommand()),
    NEXT_COURSE(new ProchainCoursCommand()),
    LISTEN_EDT(new EcouteEdtCommand());

    private final Command command;

    Commands(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }
}
