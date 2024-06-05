package com.ren1kron.server.commandRealization.commands.authorization;

import com.ren1kron.common.network.User;
import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.LoginRequest;
import com.ren1kron.common.network.requestDecorators.Response;
import com.ren1kron.server.commandRealization.Command;
import com.ren1kron.server.managers.UserManager;

public class RegisterCommand extends Command {
    private UserManager userManager;
    public RegisterCommand(UserManager userManager) {
        super("register", "Used for user registration");
        this.userManager = userManager;
    }
    @Override
    public Response apply(Sendable request) {
        LoginRequest loginRequest = (LoginRequest) request;
        User user = loginRequest.getUser();

        if (UserManager.addUser(user)) return new Response("You successfully registered");
        else return new Response(false, "Something went wrong. You was not able to register. " +
                "Maybe, where are someone with your login already?");
    }
}
