package com.ren1kron.server.commandRealization.commands;


import com.ren1kron.common.console.Console;
import com.ren1kron.common.models.Worker;
import com.ren1kron.common.network.Request;
import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.ElementRequest;
import com.ren1kron.common.network.requestDecorators.Response;
import com.ren1kron.server.commandRealization.Command;
import com.ren1kron.server.managers.CollectionManager;

import java.util.Map;

/**
 * Command 'remove_lower {element}'. Remove from collection all elements smaller than specified one.
 * @author ren1kron
 */
public class RemoveLowerCommand extends Command {
    private final CollectionManager collectionManager;
    private final Console console;
    public RemoveLowerCommand(Console console, CollectionManager collectionManager) {
        super("remove_lower {element}", "Remove from collection all elements smaller than specified one");
        this.collectionManager = collectionManager;
        this.console = console;
    }

    /**
     * Applies command
     * @param request Arguments for applying command
     * @return Command status
     */
    @Override
    public Response apply(Sendable request) {
//        if (!request.getStatus().equals(RequestStatus.ELEMENT_COMMAND))
//            return new Request("Wrong amount of arguments!\nYou suppose to write: '" + getName() + "'");

//        console.println("* Getting the worker to compare...");
        ElementRequest elementRequest = (ElementRequest) request;

        Worker worker = (Worker) elementRequest.element();
        String username = elementRequest.username();
//        try {
//            var key = Asker.askKey(console);
//            worker = Asker.askWorker(console, key, collectionManager.getFreeId());
//            if (worker == null) throw new NullPointerException();
//        } catch (AskExitExecption e) {
//            return new ExecutionResponse(false, "Abort the operation...");
//        } catch (NullPointerException e) {
//            return new ExecutionResponse(false, "This is not a worker! Abort the operation...");
//        }
//        for (var e : collectionManager.getKeyMap().values()) {
////            if (worker.compareTo(e) < 0) collectionManager.removeByKey(e.getKey());
//            if (worker.getSalary() > e.getSalary()) collectionManager.removeByKey(e.getKey());
//        }
        collectionManager.getKeyMap().entrySet().stream()
//                .filter(element -> element.getValue().getSalary() < worker.getSalary())
//                .filter(e -> e.getValue().getUsername().equals(username))
                .filter(element -> (element.getValue().getSalary() < worker.getSalary()) && (element.getValue().getUsername().equals(username)))
                .map(Map.Entry::getKey)
                .toList()
                .forEach(collectionManager::removeByKey);
//        return new ExecutionResponse("All workers with ID lower than specified was deleted!");
//        return new Response(new Request("All workers with salary lower than specified one was deleted!"));
        return new Response(new Request("All workers created by this user with salary lower than specified one was deleted!"));
    }
}
