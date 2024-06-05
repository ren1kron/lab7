package com.ren1kron.server.commandRealization.interfaces;

//import general.network.depricated.Request;

import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.Response;

/**
 * All executable commands implement this interface
 * @author ren1kron
 */
public interface Executable {
    /**
     * Apply command
     *
     * @param arguments Arguments for applying command
     * @return result of executing command
     */
    Response apply(Sendable request);
}
