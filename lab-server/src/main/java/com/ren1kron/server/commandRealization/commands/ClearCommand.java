package com.ren1kron.server.commandRealization.commands;


import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.UsernameRequest;
import com.ren1kron.common.network.requestDecorators.Response;
import com.ren1kron.server.commandRealization.Command;
import com.ren1kron.server.managers.CollectionManager;

/**
 * Command 'clear'. This command removes from collection all workers, added by client which applied it.
 * @author ren1kron
 */
public class ClearCommand extends Command {
    private final CollectionManager collectionManager;
    public ClearCommand(CollectionManager collectionManager) {
        super("clear", "Clears the collection from workers, added by user");
        this.collectionManager = collectionManager;
    }

    /**
     * Applies command
     *
     * @param request Arguments for applying command
     * @return Command status
     */
    @Override
    public Response apply(Sendable request) {
//        if (!request.getStatus().equals(RequestStatus.NORMAL)) return new Request("Wrong amount of arguments!\nYou suppose to write: '" + getName() + "'");
//        collectionManager.clear();
//        return new Response("Collection was cleared!");
        UsernameRequest usernameRequest = (UsernameRequest) request;
        collectionManager.removeAllByUser(usernameRequest.username());
        return new Response("All workers of this client were successfully deleted");
    }
}
