package com.ren1kron.server.commandRealization.commands.serverOnly;


import com.ren1kron.common.console.Console;
import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.Response;
import com.ren1kron.server.commandRealization.Command;
import com.ren1kron.server.commandRealization.interfaces.ServerCommand;

/**
 * Command 'exit'. Closes the application without saving the collection to csv-file.
 * @author ren1kron
 */
public class ExitCommand extends Command implements ServerCommand {
    private final Console console;
    public ExitCommand(Console console) {
//        super("exit", "Closes the application without saving the collection to csv-file");
//        super("exit", "If you client - disconnects you from server. If you server - closes server without saving collection.");
        super("exit", "If you client - disconnects you from server. If you server - closes server.");
        this.console = console;
    }

    /**
     * Applies command
     * @param request Arguments for applying command
     * @return Command status
     */

    @Override
    public Response apply(Sendable request) {
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        console.println("Closing server...");
        System.exit(1);
        return null;
    }
}
