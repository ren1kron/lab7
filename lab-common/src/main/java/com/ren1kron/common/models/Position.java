package com.ren1kron.common.models;

import java.io.Serializable;

public enum Position implements Serializable {
    MANAGER,
    LABORER,
    MAID_IN_STOCKINGS,
    BAKER;
    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (var position : values()) {
            nameList.append(position.name()).append(", ");
        }
        return nameList.substring(0, nameList.length() - 2);
    }
}
