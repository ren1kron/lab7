package com.ren1kron.common.network.requestDecorators.abstractions;


import com.ren1kron.common.network.abstractions.Sendable;

public abstract class SendableDecorator implements Sendable {
    private Sendable sendable;
    public SendableDecorator(Sendable sendable) {
        this.sendable = sendable;
    }
    @Override
    public String message() {
        return sendable.message();
    }
    public Sendable getSendable() {
        return this.sendable;
    }
    public void setSendable(Sendable sendable) {
        this.sendable = sendable;
    }
}
