package com.ren1kron.server;


import com.ren1kron.common.console.StandardConsole;
import com.ren1kron.server.commandRealization.commands.authorization.LoginCommand;
import com.ren1kron.server.commandRealization.commands.authorization.RegisterCommand;
import com.ren1kron.server.managers.*;
import com.ren1kron.server.commandRealization.commands.serverOnly.*;
import com.ren1kron.server.commandRealization.commands.*;
import com.ren1kron.server.network.TcpServerManager;
import com.ren1kron.server.utility.CommandExecutor;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/lab7";
    private static final String USER = "ren1kron";
    private static final String PASSWORD = "new_password";
//    private static final String DB_URL = "jdbc:postgresql://localhost:5432/studs";
//    private static final String USER = "s409411";
//    private static final String PASSWORD = "***";
    public static void main(String[] args) {
        StandardConsole console = new StandardConsole();
//        String fileName = System.getenv(ENV_KEY);
//        DumpManager dumpManager = new DumpManager(fileName, console);
        DatabaseManager databaseManager = new DatabaseManager(DB_URL, USER, PASSWORD);
        CollectionManager collectionManager = new CollectionManager(databaseManager);
        UserManager userManager = new UserManager(DB_URL, USER, PASSWORD);
        if (!collectionManager.init()) {
            console.printError("Not valid data in loaded file!");
            System.exit(1);
        }

        var commandManager = new CommandManager() {{
            register("help", new HelpCommand(this));
            register("info", new InfoCommand(collectionManager));
            register("show", new ShowCommand(collectionManager));
            register("insert", new InsertCommand(console, collectionManager));
            register("update", new UpdateCommand(console,collectionManager));
            register("remove_key", new RemoveKeyCommand(collectionManager));
            register("clear", new ClearCommand(collectionManager));
//            register("save", new SaveCommand(collectionManager));
//            register("execute_script", new ExecuteScriptCommand());
            register("exit", new ExitCommand(console));
            register("remove_lower", new RemoveLowerCommand(console, collectionManager));
            register("replace_if_greater", new ReplaceIfGreaterCommand(console, collectionManager));
            register("remove_greater_key", new RemoveGreaterKeyCommand(console, collectionManager));
            register("group_counting_by_creation_date", new GroupCountingByCreationDateCommand(collectionManager));
            register("filter_by_position", new FilterByPosition(console, collectionManager));
            register("print_field_descending_salary", new PrintFieldDescendingSalaryCommand(collectionManager));
            register("login", new LoginCommand(userManager));
            register("register", new RegisterCommand(userManager));
        }};
//         TODO сделать два класса, которые как-то с собой взаимодействуют.
//          один из них – подготавливает данные, а другой – их от него получает (пока без сети)
//          затем сделать простую программу, которая обменивается инфой по сети
        InetSocketAddress address = new InetSocketAddress("localhost", 26133);

        CommandExecutor commandExecutor = new CommandExecutor(commandManager);
        TcpServerManager tcpServer = new TcpServerManager(address, commandExecutor, console);
        tcpServer.start();
//        CollectionManager.test(collectionManager);
    }
}
