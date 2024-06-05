package com.ren1kron.server.commandRealization.commands;


//import general.network.deprecated.Request;
import com.ren1kron.common.network.Request;
import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.Response;
import com.ren1kron.server.commandRealization.Command;
import com.ren1kron.server.managers.CollectionManager;

/**
 * Command 'show'. Displays all elements of collection
 * @author ren1kron
 */
public class ShowCommand extends Command {
    private final CollectionManager collectionManager;
    public ShowCommand(CollectionManager collectionManager) {
        super("show", "Displays all elements of collection");
        this.collectionManager = collectionManager;
    }

    /**
     * Applies command
     * @param request Arguments for applying command
     * @return Command status
     */
    @Override
    public Response apply(Sendable request) {
//        if (!request.getStatus().equals(RequestStatus.NORMAL)) return new Request("Wrong amount of arguments!\nYou suppose to write: '" + getName() + "'");

        return new Response(new Request(collectionManager.toString()));
    }
}
