package com.ren1kron.server.commandRealization.commands;


import com.ren1kron.common.console.Console;
import com.ren1kron.common.network.Request;
import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.KeyRequest;
import com.ren1kron.common.network.requestDecorators.Response;
import com.ren1kron.server.commandRealization.Command;
import com.ren1kron.server.managers.CollectionManager;

/**
 * Command 'remove_greater_key key'. Removes all elements with key greater than the specified one from collection.
 * @author ren1kron
 */
public class RemoveGreaterKeyCommand extends Command {
    private final Console console;
    private final CollectionManager collectionManager;
    public RemoveGreaterKeyCommand(Console console, CollectionManager collectionManager) {
        super("remove_greater_key key", "Removes all elements with key greater than the specified one from collection");
        this.console = console;
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

//        int key = Integer.parseInt(arguments[1].trim());
        KeyRequest keyRequest = (KeyRequest) request;
        int key = keyRequest.key();
        String username = keyRequest.username();

        // IDE recommended way
//        collectionManager.getKeyMap().keySet().removeIf(integer -> key < integer);
        collectionManager.getKeyMap().entrySet().removeIf(entry -> (key < entry.getKey()) && (entry.getValue().getUsername().equals(username)));
        // harder way
//            Iterator<Integer> iterator = collectionManager.getKeyMap().keySet().iterator();
//            while (iterator.hasNext()) {
//                if (key < iterator.next()) iterator.remove();
//            }
        // easy way
//            List<Integer> list = new LinkedList<>();
//            for (Integer e : collectionManager.getKeyMap().keySet()) {
//                if (key < e) list.add(e);
//            }
//            for (Integer i : list) collectionManager.removeByKey(i);
        return new Response(new Request("All elements with key greater than specified one has been successfully removed from collection!"));
    }
}
