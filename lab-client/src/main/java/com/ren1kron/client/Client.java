package com.ren1kron.client;


import com.ren1kron.client.network.TcpClientManager;
import com.ren1kron.client.utility.Requester;
import com.ren1kron.common.console.Console;
import com.ren1kron.common.console.StandardConsole;

import java.net.InetSocketAddress;

/**
 * Main class for client
 * @author ren1kron
 */
public class Client {
    public static void main(String[] args) {

        InetSocketAddress address = new InetSocketAddress("localhost", 26133);
        Console console = new StandardConsole();
        TcpClientManager tcpClient = new TcpClientManager(console, address);

        tcpClient.start();


        Requester requester = new Requester(console, tcpClient);
//        requester.interactiveMode();
        requester.loginMode();

    }
}
