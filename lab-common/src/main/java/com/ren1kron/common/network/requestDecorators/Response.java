package com.ren1kron.common.network.requestDecorators;

import com.ren1kron.common.network.Request;
import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.abstractions.SendableDecorator;

public class Response extends SendableDecorator {
    private static final long serialVersionUID = 1001L;
    private boolean status;
//    private Sendable request;
//    private Sendable request;
//    public Response(boolean status, Sendable request) {
//        this.status = status;
//        this.request = request;
//    }
    public Response(boolean status, Sendable sendable) {
        super(sendable);
        this.status = status;
    }
    public Response(boolean status, String message) {
        super(new Request(message));
        this.status = status;
    }
    public Response(Sendable request) {
        this(true, request);
    }
    public Response(String message) {
        this(true, message);
    }

//    @Override
//    public String message() {
//        return request.message();
//    }

    public boolean isSuccessful() {
        return status;
    }
}


