package com.ren1kron.server.commandRealization.commands;

import com.ren1kron.common.network.Request;
import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.Response;
import com.ren1kron.server.commandRealization.Command;
import com.ren1kron.server.managers.CommandManager;

import java.util.stream.Collectors;

/**
 * Command 'help'. This command displays help for available commands
 * @author ren1kron
 */
public class HelpCommand extends Command {
    private final CommandManager commandManager;
    public HelpCommand(CommandManager commandManager) {
        super("help", "Displays help for available commands");
        this.commandManager = commandManager;
    }

    /**
     * Applies command
     * @param request Arguments for applying command
     * @return Command status
     */
    @Override
    public Response apply(Sendable request) {
//        if (request.getKey().equals(null)) return new ExecutionResponse(false, "Wrong amount of arguments!\nYou suppose to write: '" + getName() + "'");
//        if (!request.getStatus().equals(RequestStatus.NORMAL)) return new Request("Wrong amount of arguments!\nYou suppose to write: '" + getName() + "'");
//        System.out.println(commandManager.getCommands().values().stream().map(command -> String.format(" %-35s%-1s%n", command.getName(), command.getDescription())).collect(Collectors.joining("\n")));
        return new Response(new Request(commandManager.getCommands().values().stream().map(command -> String.format(" %-35s%-1s%n", command.getName(), command.getDescription())).collect(Collectors.joining("\n"))));
    }
}
