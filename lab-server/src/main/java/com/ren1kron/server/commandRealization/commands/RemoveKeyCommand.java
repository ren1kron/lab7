package com.ren1kron.server.commandRealization.commands;


import com.ren1kron.common.network.Request;
import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.KeyRequest;
import com.ren1kron.common.network.requestDecorators.Response;
import com.ren1kron.server.commandRealization.Command;
import com.ren1kron.server.managers.CollectionManager;

/**
 * Command 'remove_key key'. Removes element from collection by its key.
 * @author ren1kron
 */
public class RemoveKeyCommand extends Command {
    private final CollectionManager collectionManager;
    public RemoveKeyCommand(CollectionManager collectionManager) {
        super("remove_key key", "Removes element from collection by key");
        this.collectionManager = collectionManager;
    }

    /**
     * Applies command
     * @param request Arguments for applying command
     * @return Command status
     */
    @Override
    public Response apply(Sendable request) {
//        if (!request.getStatus().equals(RequestStatus.KEY_COMMAND))
//            return new Request("Wrong amount of arguments!\nYou suppose to write: '" + getName() + "'");

        KeyRequest keyRequest = (KeyRequest) request;
        int key = keyRequest.key();
        String username = keyRequest.username();

//        System.out.println(key);
        if (collectionManager.byKey(key).getUsername().equals(username)) {
            if (collectionManager.removeByKey(key))
                return new Response(new Request("Element was successfully deleted!"));
            return new Response(false, new Request("Element with selected key is not exist"));
        }
        return new Response(false, new Request("This element was created by another user"));
    }
}
