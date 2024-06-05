package com.ren1kron.server.commandRealization.commands;



import com.ren1kron.common.console.Console;
import com.ren1kron.common.models.Worker;
import com.ren1kron.common.network.Request;
import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.ElementRequest;
import com.ren1kron.common.network.requestDecorators.Response;
import com.ren1kron.server.commandRealization.Command;
import com.ren1kron.server.managers.CollectionManager;

import java.util.NoSuchElementException;

/**
 * Command 'update id {element}'. This command updates element with inserted id
 * @author ren1kron
 */
public class UpdateCommand extends Command {
    private final Console console;
    private final CollectionManager collectionManager;
    public UpdateCommand(Console console, CollectionManager collectionManager) {
        super("update id {element}", "Update element of collection with inserted id");
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
//        if (!request.getStatus().equals(RequestStatus.KEY_ELEMENT_COMMAND))
//            return new Request("Wrong amount of arguments!\nYou suppose to write: '" + getName() + "'");


        ElementRequest elementRequest = (ElementRequest) request;
        Worker worker = (Worker) elementRequest.element();
        String username = elementRequest.username();
//        KeyRequest keyRequest = (KeyRequest) request;
//        int id = keyRequest.key();
        try {
            int id = elementRequest.key();
            worker.setId(id);

            if (!(id > 0)) return new Response(false, new Request("Selected id is invalid!"));

            var oldWorker = collectionManager.byId(id);
            if (oldWorker == null || !collectionManager.isContain(oldWorker))
                return new Response(false, "Worker with the specified ID does not exist!");
            if (!oldWorker.getUsername().equals(username))
                return new Response(false, "This worker was created by another user. You don't have permissions to update it");
            var key = oldWorker.getKey();
            worker.setKey(key);

//        Worker worker = (Worker) request.getElement();

            if (worker.validate()) {
                collectionManager.removeByKey(key);
                collectionManager.add(worker);
                return new Response("Worker with inserted id was successfully updated!");
            } else
                return new Response(false, "Fields of inserted old Worker are invalid. Worker wasn't updated!");
        } catch (NoSuchElementException e) {
            return new Response(false, "The request received from the client is incorrect. There is no key in it!");
        }
    }
}
