package com.ren1kron.client;


import com.ren1kron.client.network.TcpClientManager;
import com.ren1kron.client.utility.Requester;
import com.ren1kron.common.console.Console;
import com.ren1kron.common.console.StandardConsole;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        Request hui = new Request("hui");
//        KeyRequest keyRequest = new KeyRequest(hui, 1);
//        Console console = new StandardConsole();
//        ElementRequest elementRequest = new ElementRequest(keyRequest, Asker.askWorker(console, 0, 0));
//
//        System.out.println(elementRequest.element());
//        Sendable wrappedSendable = elementRequest;
//        KeyRequest wrappedKey = (KeyRequest) wrappedSendable;
//        System.out.println(keyRequest.key());

        InetSocketAddress address = new InetSocketAddress("localhost", 26133);
//        TcpClientManager tcpClient = new TcpClientManager(address);
        Console console = new StandardConsole();
        TcpClientManager tcpClient = new TcpClientManager(console, address);

        tcpClient.start();


        Requester requester = new Requester(console, tcpClient);
//        requester.interactiveMode();
        requester.loginMode();

    }
}
