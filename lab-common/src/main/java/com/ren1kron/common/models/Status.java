package com.ren1kron.common.models;

import java.io.Serializable;

public enum Status implements Serializable {
    FIRED,
    HIRED,
    REGULAR;
    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (var status : values()) {
            nameList.append(status.name()).append(", ");
        }
        return nameList.substring(0, nameList.length() - 2);
    }
}
