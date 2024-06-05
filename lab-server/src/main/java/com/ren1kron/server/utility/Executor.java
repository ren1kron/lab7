package com.ren1kron.server.utility;

//import general.network.depricated.Request;

import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.Response;
import com.ren1kron.server.managers.CommandManager;

public interface Executor {
    Response execute(Sendable request);
    CommandManager getCommandManager();
}
