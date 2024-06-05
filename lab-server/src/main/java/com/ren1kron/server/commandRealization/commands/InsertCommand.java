package com.ren1kron.server.commandRealization.commands;


import com.ren1kron.common.console.Console;
import com.ren1kron.common.models.Worker;
import com.ren1kron.common.models.abstractions.Element;
import com.ren1kron.common.network.Request;
import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.ElementRequest;
import com.ren1kron.common.network.requestDecorators.Response;
import com.ren1kron.common.network.requestDecorators.abstractions.SendableDecorator;
import com.ren1kron.server.commandRealization.Command;
import com.ren1kron.server.managers.CollectionManager;

import java.util.NoSuchElementException;

/**
 * Command 'insert key {element}'. Adds to collection new worker with inserted key
 * @author ren1kron
 */
public class InsertCommand extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public InsertCommand(Console console, CollectionManager collectionManager) {
        super("insert key {element}", "Adds new element with specified key in collection");
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
        SendableDecorator request1 = (SendableDecorator) request;
        ElementRequest elementRequest = (ElementRequest) request;
        Element element = elementRequest.element();

//        KeyRequest keyRequest = (KeyRequest) request1;
//        int key = keyRequest.key();
//        int key;
        try {
            int key = elementRequest.key();


            if (!(key > 0)) return new Response(false, new Request("Not valid or empty key"));
            if (element == null) return new Response(false, new Request("Empty element"));

            Worker worker = (Worker) element;
//        int key = request.getKey();
            if (collectionManager.byKey(key) != null)
                return new Response(false, new Request("Worker with specified key already exist!"));
//        Worker worker = (Worker) request.getElement();
//        if (worker != null) worker.setId(collectionManager.getFreeId());
            worker.setId(collectionManager.getFreeId());
            worker.setKey(key);
            if (!worker.validate())
                return new Response(false, new Request("Fields of inserted worker are invalid. Worker wasn't added to collection"));
//        if (worker != null && worker.validate()) {
            else {
                boolean added = collectionManager.add(worker);
                if (added) return new Response("Worker was successfully added to collection!");
                else return new Response(false, "Something went wrong while adding worker!");
            }
        } catch (NoSuchElementException e) {
            return new Response(false, new Request("The request received from the client is incorrect. There is no key in it!"));
        }
    }
}
