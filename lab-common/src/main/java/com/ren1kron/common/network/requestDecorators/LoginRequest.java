package com.ren1kron.common.network.requestDecorators;

import com.ren1kron.common.network.Request;
import com.ren1kron.common.network.User;
import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.abstractions.SendableDecorator;

public class LoginRequest extends SendableDecorator {
//    private boolean registered;
    private User user;

//    public LoginRequest(boolean registered, User user, Sendable request) {
//        super(request);
//        this.registered = registered;
//        this.user = user;
//    }
    public LoginRequest(User user, Sendable request) {
        super(request);
        this.user = user;
    }
    public LoginRequest(User user, String message) {
//        super(new Request(message));
//        this.user = user;
        this(user, new Request(message));
    }
//    public LoginRequest(User user, Sendable request) {
//        this(true, user, request);
//    }
//    public boolean isRegistered() {
//        return registered;
//    }

    public User getUser() {
        return user;
    }
}
