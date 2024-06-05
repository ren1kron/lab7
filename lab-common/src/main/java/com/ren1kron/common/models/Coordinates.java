package com.ren1kron.common.models;



import com.ren1kron.common.models.abstractions.Validatable;

import java.io.Serializable;

public class Coordinates implements Validatable, Serializable {
    private float x; // Максимальное значение поля: 145
    private double y;
    public Coordinates(float x, double y) {
        this.x = x;
        this.y = y;
    }
    public Coordinates(String string) {
        try {
            try {
                this.x = Float.parseFloat(string.split(";")[0]);
            } catch (NumberFormatException ignored) { }
            try {
                this.y = Double.parseDouble(string.split(";")[1]);
            } catch (NumberFormatException ignored) { }
        } catch (ArrayIndexOutOfBoundsException ignored) { }
    }

    public double getY() {
        return y;
    }

    public float getX() {
        return x;
    }
    @Override
    public boolean validate() {
        return (x <= 145);
    }
    @Override
    public String toString() {
        return x + ";" + y;
    }
}
