package reminator.EdtBot.exceptions;

import reminator.EdtBot.Commands.argument.Argument;

public class ArgumentFormatException extends Exception {

    private Argument<?> argument;
    private Object value;

    public ArgumentFormatException(Argument<?> argument, Object value) {
        this.argument = argument;
        this.value = value;
    }

    @Override
    public String getMessage() {
        return "L'argument `" + value + "` n'est pas du type **" + argument.getType().getSimpleName() + "** !!! <:poulpePasContent:772185062618824714>";
    }
}
