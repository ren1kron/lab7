package com.ren1kron.server.commandRealization.commands.authorization;

import com.ren1kron.common.network.User;
import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.LoginRequest;
import com.ren1kron.common.network.requestDecorators.Response;
import com.ren1kron.server.commandRealization.Command;
import com.ren1kron.server.managers.UserManager;

public class LoginCommand extends Command {
    private UserManager userManager;
    public LoginCommand(UserManager userManager) {
        super("login", "Used for user authorization");
        this.userManager = userManager;
    }
    @Override
    public Response apply(Sendable request) {
        LoginRequest loginRequest = (LoginRequest) request;
        User user = loginRequest.getUser();

        if (userManager.checkUser(user)) {
            return new Response("You have been successfully logged in");
        } else return new Response(false, "There is no such user");
    }
}
