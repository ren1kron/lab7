package com.ren1kron.common.network;


import com.ren1kron.common.network.abstractions.Sendable;

import java.io.Serializable;

public record Request(String message) implements Sendable, Serializable {
    private static final long serialVersionUID = 100L;
}
