package com.ren1kron.server.utility;

//import general.network.deprecated.Request;
import com.ren1kron.common.network.Request;
import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.Response;
import com.ren1kron.server.commandRealization.Command;
import com.ren1kron.server.managers.CommandManager;

public class CommandExecutor implements Executor{
    private final CommandManager commandManager;

    public CommandExecutor(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

//    public void interactiveMode() {
//        String[] userInput;
//        while (true) {
////            console.prompt();
//            userInput = (console.readln().trim()).split(" ");
//            if (userInput.length > 1) userInput[1] = userInput[1].trim();
//
//            if (userInput[0].isEmpty() || userInput.length > 1) {
//                console.println("");
//                console.prompt();
//                continue;
//            }
//
//            console.println(commandManager.getCommands().get(userInput[0]).apply(null));
//
//        }
//    }

    @Override
    public Response execute(Sendable request) {
//        Command command = commandManager.getCommands().get(request.getMessage());
        Command command = commandManager.getCommands().get(request.message());

        if (command == null) return new Response(false, new Request("Inserted command is not exist or you do not have permission to use it"));
        return command.apply(request);
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }
}
