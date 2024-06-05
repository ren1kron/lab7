package com.ren1kron.common.network.requestDecorators;


import com.ren1kron.common.models.abstractions.Element;
import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.abstractions.SendableDecorator;

import java.util.NoSuchElementException;

public class ElementRequest extends SendableDecorator {
    private static final long serialVersionUID = 1004L;
//    private Request request;
//    private Sendable request;
    private Element element;
//    private Sendable sendable;

    public ElementRequest(Sendable sendable, Element element) {
        super(sendable);
        this.element = element;
    }
//    public ElementRequest(Sendable request, Element element) {
//        this.request = request;
//        this.element = element;
//    }
//    @Override
//    public String message() {
//        return request.message();
//    }
    public Element element() {
        return element;
    }
    public int key() {
        Sendable sendable = getSendable();
        if (sendable instanceof KeyRequest) {
            return ((KeyRequest) sendable).key();
        }
//        return 0;
        throw new NoSuchElementException();
    }
    public String username() {
        Sendable sendable = getSendable();
        if (sendable instanceof UsernameRequest) {
            return ((UsernameRequest) sendable).username();
        }
        if (sendable instanceof KeyRequest && ((KeyRequest) sendable).username() != null) {
            return ((KeyRequest) sendable).username();
        }
//        return null;
        throw new NoSuchElementException();
    }
//    private Request request;
//    private final Element element;
//
//    public ElementRequest(Request request, Element element) {
//        this.element = element;
//    }
//
//    @Override
//    public String getMessage() {
//        return request.getMessage();
//    }
//
//    public Element getElement() {
//        return element;
//    }
}
