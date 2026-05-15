package za.co.wethinkcode.robots.command;

import java.util.Set;


public class CommandProcessor {

    private static final Set<String> VALID_COMMANDS = Set.of(
            "launch", "look", "forward", "back",
            "turn", "state", "repair", "reload", "fire"
    );


    public String execute(String command) {
        if (VALID_COMMANDS.contains(command.toLowerCase())) {
            return "OK";
        }
        return "ERROR";
    }
}