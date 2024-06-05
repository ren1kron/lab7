package com.ren1kron.server.commandRealization;


import com.ren1kron.server.commandRealization.interfaces.Describable;
import com.ren1kron.server.commandRealization.interfaces.Executable;

import java.util.Objects;

public abstract class Command implements Executable, Describable {
    private final String name;
    private final String description;
    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public String getDescription() {
        return description;
    }
    @Override
    public String toString() {
//        return "Name of Command: " + name + "; Description of Command:" + description;
        return "Command{name='" + name + "', description='" + description + "'}";
    }
    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o==null || getClass()!=o.getClass()) return false;
        Command command = (Command) o;
        return name.equals(command.getName()) && description.equals(command.getDescription());
    }
}
