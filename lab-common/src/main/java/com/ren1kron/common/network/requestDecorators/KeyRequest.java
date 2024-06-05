package com.ren1kron.common.network.requestDecorators;


import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.abstractions.SendableDecorator;

import java.util.NoSuchElementException;

public class KeyRequest extends SendableDecorator {
    private static final long serialVersionUID = 100003L;
//    private Request request;
//    private Sendable request;
    private int key;

    public KeyRequest(Sendable sendable, int key) {
        super(sendable);
        this.key = key;
    }

    //    public KeyRequest(Sendable request, int key) {
//        this.request = request;
//        this.key = key;
//    }
//    @Override
//    public String message() {
//        return request.message();
//    }

    public int key() {
        return key;
    }
    public String username() {
        Sendable sendable = getSendable();
        if (sendable instanceof UsernameRequest) {
            return ((UsernameRequest) sendable).username();
        }
//        return null;
        throw new NoSuchElementException();
    }
}
