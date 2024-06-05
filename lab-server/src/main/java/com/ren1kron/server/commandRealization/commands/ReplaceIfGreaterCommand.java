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
 * Command 'replace_if_greater key {element}'. Replaces element with specified key if new element bigger than old one
 * @author ren1kron
 */
public class ReplaceIfGreaterCommand extends Command {
    private final Console console;
    private final CollectionManager collectionManager;
    public ReplaceIfGreaterCommand(Console console, CollectionManager collectionManager) {
        super("replace_if_greater key {element}", "Replaces element with specified key if new element bigger than old one");
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

        ElementRequest elementRequest = (ElementRequest) request;
        Worker newWorker = (Worker) elementRequest.element();
        String username = elementRequest.username();
//        KeyRequest keyRequest = (KeyRequest) request;
//        int key = keyRequest.key();
        try {
            int key = elementRequest.key();
            newWorker.setKey(key);
            if (key <= 0 || !newWorker.validateWithoutId())
                return new Response(false, new Request("Key or fields of inserted worker are invalid. Worker wasn't updated."));

            Worker oldWorker = collectionManager.byKey(key);
            if (oldWorker == null || !collectionManager.isContain(oldWorker))
                return new Response(false, new Request("Worker with the specified key does not exist. If you want to add newWorker with this key anyway, use command 'insert key {element}"));
            if (!oldWorker.getUsername().equals(username))
                return new Response(false, "Worker with the specified key was created by another user. You have not permission to change it.");
            var id = oldWorker.getId();

            newWorker.setId(id);



            if (newWorker.validate() && (newWorker.getSalary() > oldWorker.getSalary())) {
                collectionManager.removeByKey(key);
                collectionManager.add(newWorker);
                return new Response(new Request("Worker with selected key was successfully replaced with new Worker!"));
//            return new ExecutionResponse("Worker with inserted id was successfully updated!");
            } else
//                return new Response(false, new Request("Old Worker has bigger salary or fields of new Worker are invalid. Collection was not updated!"));
                return new Response(false, new Request("New Worker has lower salary than old one. Collection was not updated!"));
//        } else return new ExecutionResponse(false, "Fields of inserted oldWorker are invalid. Worker wasn't updated!");
        } catch (NoSuchElementException e) {
            return new Response(false, new Request("The request received from the client is incorrect. There is no key in it!"));
        }
    }
}
