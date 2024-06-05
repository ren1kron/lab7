package com.ren1kron.common.models.abstractions;

import java.io.Serializable;

public abstract class Element implements Serializable, Validatable, Comparable<Element>{
    private static final long serialVersionUID = 11234L;
    abstract public int getId();
}
