package com.ren1kron.server.commandRealization.commands;


//import general.network.depricated.Request;
import com.ren1kron.common.network.Request;
import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.Response;
import com.ren1kron.server.commandRealization.Command;
import com.ren1kron.server.managers.CollectionManager;

/**
 * Command 'info'. This displays information about the collection
 * @author ren1kron
 */
public class InfoCommand extends Command {
    private final CollectionManager collectionManager;
    public InfoCommand(CollectionManager collectionManager) {
        super("info", "Display information about the collection");
        this.collectionManager = collectionManager;
    }

    /**
     * Applies command
     *
     * @param request Argument for applying command
     * @return Command status
     */
    @Override
//    public Response apply(String[] arguments) {
    public Response apply(Sendable request) {
//        if (request.getKey().equals(null)) return new Response(false, "Wrong amount of arguments!\nYou suppose to write: '" + getName() + "'");
//        if (!request.getStatus().equals(RequestStatus.NORMAL)) return new Request("Wrong amount of arguments!\nYou suppose to write: '" + getName() + "'");


        String initTime = (collectionManager.getLastInitTime() == null) ? "collection hasn't been initialized in this session yet" :
                "Date: " + collectionManager.getLastInitTime().toLocalDate().toString() + " | Time: " + collectionManager.getLastInitTime().toLocalTime().toString();


        var s = "Info about collection\n";
        s += "Type of collection: " + collectionManager.getKeyMap().getClass() + "\n";
        s += "Last initialization time: " + initTime + "\n";
        s += "Amount of workers in collection: " + collectionManager.getKeyMap().size() + "\n";


        return new Response(new Request(s));
    }
}
