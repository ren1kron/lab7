package com.ren1kron.common.network.requestDecorators;


import com.ren1kron.common.models.Position;
import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.abstractions.SendableDecorator;

public class PositionRequest extends SendableDecorator {
    private static final long serialVersionUID = 1002L;
    private Position position;

    public PositionRequest(Sendable sendable, Position position) {
        super(sendable);
        this.position = position;
    }


    public Position getPosition() {
        return position;
    }
}
