package com.ren1kron.common.network.requestDecorators;

import com.ren1kron.common.network.Request;
import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.abstractions.SendableDecorator;

public class UsernameRequest extends SendableDecorator {
    private String username;

    public UsernameRequest(String username, Sendable request) {
        super(request);
        this.username = username;
    }
    public UsernameRequest(String username, String message) {
        this(username, new Request(message));
    }

    public String username() {
        return username;
    }

}
