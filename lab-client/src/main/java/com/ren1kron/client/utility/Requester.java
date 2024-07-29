package com.ren1kron.client.utility;


import com.ren1kron.client.network.TcpClientManager;
import com.ren1kron.common.console.Console;
import com.ren1kron.common.exceptions.AskExitException;
import com.ren1kron.common.models.Position;
import com.ren1kron.common.models.Worker;
import com.ren1kron.common.network.Request;
import com.ren1kron.common.network.User;
import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Requester {
//    private final static String[] key_commands = {"insert"};
    private final static List<String> key_commands = List.of("insert", "remove_key", "replace_if_greater", "remove_greater_key", "update");
//    private final static List<String> key_commands = List.of("insert", "remove_key", "replace_if_greater", "remove_greater_key");
    private final static List<String> element_commands = List.of("insert", "update", "remove_lower", "replace_if_greater");
//    private final static List<String> id_commands = List.of("update");
    private final static List<String> position_commands = List.of("filter_by_position");
    private final static List<String> username_commands = List.of("clear", "remove_key", "replace_if_greater", "remove_lower", "remove_greater_key", "update");
//    private final static String[] id_commands = {"update"};
    private final List<String> scriptStack = new ArrayList<>();
    private Console console;
    private TcpClientManager client;
    private String login;

    public Requester(Console console, TcpClientManager client) {
        this.console = console;
        this.client = client;
    }

    public void loginMode() {
        String userInput;
        boolean exit = false;
//        console.println("––– Hello ---");
        console.println("Type 'login' to Log in or 'register' to Sign up");
        while (!exit) {
            userInput = console.readln().trim();
            if (userInput.equals("login") || userInput.equals("register")) {
                String username = null;
                String password = null;
                while (username == null) {
                    console.print("username: ");
                    username = console.readln().trim();
                }
                while (password == null) {
                    console.print("password: ");
                    password = console.readln().trim();
                }
                synchronized (this) {
                    boolean responseStatus = client.sendRequest(new LoginRequest(new User(username, password), userInput));
                    if (responseStatus) {
                        console.println("--- You logged in as '" + username + "' ---\n" +
                                "       --- WELCOME ---\n" +
                                "Enter command 'help' for help'");
                        login = username;
//                        console.println("--- You logged in as " + username + " ---");
                        interactiveMode();
                    }
                }
            }
//            else if (userInput.equals("register")) {
//
//            }
            else if (userInput.equals("exit")) {
                exit = true;
                console.println("System exit...");
            }
            else console.println("You can't use commands until you log in.");
        }
    }
    public void interactiveMode() {
        String[] userInput;
        boolean exit = false;
        while (!exit) {
            console.prompt();
            userInput = (console.readln().trim()).split(" ");
            if (userInput.length > 1) userInput[1] = userInput[1].trim();

            if (userInput[0].isEmpty()) continue;
            else if (userInput[0].equals("exit")) {
                console.printError("Closing client...");
                System.exit(1);
            }

            if (key_commands.contains(userInput[0]) || userInput[0].equals("execute_script")) {
                if (userInput.length != 2) {
                    console.printError("Wrong amount of arguments! Write 'help' for help");
                    continue;
                }
            } else {
                if (userInput.length > 1) {
                    console.printError("Wrong amount of arguments! Write 'help' for help");
                    continue;
                }
            }
            executeCommand(userInput);

        }
    }
    private void executeCommand(String[] userCommand) {
        try {
//            Request request;
            String command = userCommand[0];
//            if (command.equals("execute_script") && userCommand.length == 1) console.printError("Wrong amount of arguments! You suppose to write 'execute_script file_name'");
            Sendable request = new Request(command);
//            Request request_build = new Request(command);
            if (command.equals("login") || command.equals("register")) {
                console.printError("You already signed up");
                return;
            }
            if (command.equals("execute_script")) {
                if (!userCommand[1].isEmpty())
                    scriptMode(userCommand[1]);
                else
                    console.printError("Wrong amount of arguments! You suppose to write 'execute_script file_name'");
                return;
            }
            if (username_commands.contains(command)) {
                request = new UsernameRequest(login, request);
            }
//            if (command.endsWith("_position")) {
            if (key_commands.contains(command)) {
                int key = Integer.parseInt(userCommand[1]);
                request = new KeyRequest(request, key);
            }
            if (position_commands.contains(command)) {
                Position position = Asker.askPosition(console);
//                if (request == null) request = new PositionRequest(request, position);
//                else request = new PositionRequest(request, position);
                request = new PositionRequest(request, position);
            }
//            if (id_commands.contains(command)) {
//                int id = Integer.parseInt(userCommand[1]);
//                request = new KeyRequest(request_build, id);
//            }
            if (element_commands.contains(command)) {
//                Worker worker = key_commands.contains(command) ? Asker.askWorker(console, 0, 0, login) : Asker.askWorker(console, Asker.askKey(console), 0, login);
                Worker worker = Asker.askWorker(console, 0, 0, login);
//                if (key_commands.contains(command)) {
//                    worker = Asker.askWorker(console, 0, 0);
//                } else
                request = new ElementRequest(request, worker);
            }
//            System.out.println(request.message());
//            if (request != null) client.sendRequest(request);
            client.sendRequest(request);
//            else client.sendRequest(request_build);



////            if (userCommand.length > 1) Integer.parseInt(userCommand[1]);
//            if (userCommand.length == 1) {
//                if (userCommand[0].equals("exit")) {
//                    console.println("Closing client...");
//                    System.exit(1);
//                }
//                if (userCommand[0].equals("execute_script")) {
//                    console.println("Wrong amount of arguments! You suppose to write 'execute_script file_name'");
////                    console.prompt();
//                    return;
//                }
//                // TODO пофиксить кринжовый костыль...
//                if (userCommand[0].equals("filter_by_position")) {
//                    Position position = Asker.askPosition(console);
//                    building_request = new Request(command, position);
//                }
//                else
//                    building_request = new Request(command);
//            }
////        Request response = client.sendRequest(building_request);
//            else {
//                // execute_script realization
//                if (userCommand[0].equals("execute_script")) {
//                    String scriptName = userCommand[1];
//                    scriptMode(scriptName);
//                    return;
////
////                    console.println(scriptOutput.getMessage());
////                    console.println("Script " + scriptName + " successfully executed!");
//
//                }
//
//                int key = Integer.parseInt(userCommand[1]);
//                if (key_commands.contains(command)) {
//                    Worker worker = Asker.askWorker(console, key, 0);
//                    building_request = new Request(command, key, worker);
//                } else if (id_commands.contains(command)) {
//                    Worker worker = Asker.askWorker(console, 0, key);
//                    building_request = new Request(command, key, worker);
//                } else building_request = new Request(command, key);
//
//            }
//        else if (userCommand.length == 2 && Arrays.asList(key_commands).contains(userCommand[0])) {
//            client.sendRequest(request);
        } catch (AskExitException e) {
            console.println("Abort the operation...");
        } catch (NumberFormatException e) {
            console.println("Inserted key is invalid!");
        }

    }
    private void scriptMode(String scriptName) {
        String[] userCommand;
//        StringBuilder executionOutput = new StringBuilder();


        if (!new File(scriptName).exists()) {
//            return new Request("This file does not exist!");
            console.printError("This file does not exist!");
            return;
        }
        if (!Files.isReadable(Paths.get(scriptName))) {
//            return new Request("You don't have access to this file!");
            console.printError("You don't have access to this file!");
            return;
        }

        try (Scanner scriptScanner = new Scanner(new File(scriptName))) {

            Request commandStatus;

            if (!scriptScanner.hasNext()) throw new NoSuchElementException();
            console.selectFileScanner(scriptScanner);
            do {
                userCommand = (console.readln().trim()).split(" ");
                if (userCommand.length > 1) userCommand[1] = userCommand[1].trim();

                if (userCommand[0].isEmpty()) {
//                    console.println("");
//                    console.prompt();
                    continue;
                }

                if (userCommand.length > 2) {
//                    console.printError("Wrong amount of arguments!");
//                    console.prompt();
                    continue;
                }
                boolean needLaunch = true;
                if (userCommand[0].equals("execute_script"))
                    needLaunch = checkRecursion(userCommand[1], scriptScanner);
//                commandStatus = needLaunch ? executeCommand(userCommand) : new Request("Recursion detected");
                executeCommand(userCommand);
            } while(!userCommand[0].equals("exit") && console.isCanReadln());
            console.selectConsoleScanner();
        } catch (FileNotFoundException e) {
//            return new Request("Script file was not found!");
            console.printError("Script file was not found!");
//            return;
        } catch (NoSuchElementException e) {
//            return new Request("Script file is empty!");
            console.printError("Script file is empty!");
//            return;
        }
    }

    private boolean checkRecursion(String argument, Scanner scriptScanner) {
        var recStart = -1;
        var i = 0;
        for (String script : scriptStack) {
            i++;
            if (argument.equals(script)) {
                if (recStart < 0) recStart = i;
//                if (lengthRecursion < 0) {
//                    console.selectConsoleScanner();
////                    console.println("Recursion was detected! Enter max size of recursion (0..500)");
////                    while (lengthRecursion < 0 || lengthRecursion > 500) {
////                        try { console.print("> "); lengthRecursion = Integer.parseInt(console.readln().trim()); } catch (NumberFormatException e) { console.println("длина не распознана"); }
////                    }
//                    console.selectFileScanner(scriptScanner);
//                }
                if (i > recStart  || i > 500)
                    return false;
            }
        }
        return true;
    }
}
