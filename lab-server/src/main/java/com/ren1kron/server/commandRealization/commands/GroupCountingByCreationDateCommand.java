package com.ren1kron.server.commandRealization.commands;


import com.ren1kron.common.models.Worker;
import com.ren1kron.common.network.Request;
import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.Response;
import com.ren1kron.server.commandRealization.Command;
import com.ren1kron.server.managers.CollectionManager;

import java.text.DateFormatSymbols;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Command 'group_counting_by_creation_date'. Group elements by their creationDate value, display amount of elements in every group
 * @author ren1kron
 */
public class GroupCountingByCreationDateCommand extends Command {
    private final CollectionManager collectionManager;
    public GroupCountingByCreationDateCommand(CollectionManager collectionManager) {
        super("group_counting_by_creation_date", "Groups elements of collection by creation date (date of employment), displays amount of elements in every group.");
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

        Map<Integer, List<Worker>> workersPerMonth = collectionManager.getKeyMap().values().stream().collect(Collectors.groupingBy(Worker::getMonth));

        var stringBuilder = new StringBuilder("––––––––––––––––––––––––––––––––––––––––––––––––––––\n");
        for (var e : workersPerMonth.keySet()) {
            var dfs = new DateFormatSymbols();
//            var workers = workersPerMonth.get(e);
            stringBuilder.append("Month: ")
                    .append(dfs.getMonths()[e])
                    .append("\n")
                    .append("Amount of workers: ")
                    .append(workersPerMonth.get(e).size())
                    .append("\n")
                    .append("––––––––––––––––––––––––––––––––––––––––––––––––––––")
                    .append("\n");
//            var worker = workersPerMonth.values().toArray()[0];
//            stringBuilder.append("Month: " + worker.get).append("\n");
            for (var worker : workersPerMonth.get(e)) {
                stringBuilder.append(worker)
                        .append("\n");
            }
            stringBuilder.append("––––––––––––––––––––––––––––––––––––––––––––––––––––")
                    .append("\n");
        }
        return new Response(new Request(stringBuilder.toString()));
    }
}
