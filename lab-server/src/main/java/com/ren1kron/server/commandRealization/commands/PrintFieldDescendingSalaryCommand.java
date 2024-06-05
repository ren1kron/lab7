package com.ren1kron.server.commandRealization.commands;


import com.ren1kron.common.network.Request;
import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.Response;
import com.ren1kron.server.commandRealization.Command;
import com.ren1kron.server.managers.CollectionManager;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Command 'print_field_descending_salary'. Displays salary values of all elements in descending order
 * @author ren1kron
 */
public class PrintFieldDescendingSalaryCommand extends Command {
    private final CollectionManager collectionManager;
    public PrintFieldDescendingSalaryCommand(CollectionManager collectionManager) {
        super("print_field_descending_salary", "Displays salary values of all elements in descending order");
        this.collectionManager = collectionManager;
    }
    /**
     * Applies command
     * @param request Arguments for applying command
     * @return Command status
     */
    @Override
    public Response apply(Sendable request) {
        var result = collectionManager.getKeyMap().entrySet().stream()
                .sorted(Map.Entry.comparingByValue((w1, w2) -> Float.compare(w2.getSalary(), w1.getSalary())))
                .map(entry -> String.valueOf(entry.getValue().getSalary()))
                .collect(Collectors.joining("; ", "* Salaries of workers in descending order: ", ""));
        return new Response(new Request(result));
    }
}
