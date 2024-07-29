package com.ren1kron.client.network;



import com.ren1kron.common.console.Console;
import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class TcpClientManager {
    private Console console;
    private InetSocketAddress address;
    private SocketChannel client;
//    private Requester requester;

    public TcpClientManager(Console console, InetSocketAddress address) {
        this.console = console;
        this.address = address;
    }
    public void start() {
        boolean connection = false;
        while (!connection) {
            try {
                client = SocketChannel.open(address);
                client.configureBlocking(false);

                console.println("You connected to server at " + address.getHostName() + ":" + address.getPort());
//                console.println("You connected to server at " + address.getHostName() + ":" + address.getPort() +
//                        "\n–––    WELCOME!!!    ––– \n" +
//                        "Enter command 'help' for help");


                connection = true;

            } catch (IOException e) {
//                System.err.println("Error while starting client : " + e.getMessage());
//                System.exit(1);
                console.printError("Unable to connect to server. We will try to reconnect in 5 seconds...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

    }
    // TODO есть идея запихнуть инициализацию стримов и буфера в старт. А потом из остальных методов их вызывать.
    //  Может, так будет эффективнее, хз.
//    public void sendRequest(Request request) {
//    public boolean sendRequest(Sendable request) {
//        boolean sendStatus = false;
//        boolean errorThrown = false;
//        do {
//            try {
//                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
//                outputStream.writeObject(request);
////            outputStream.flush();
//                outputStream.close();
//                ByteBuffer buffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
//                while (buffer.hasRemaining()) {
//                    client.write(buffer);
////                client.read(buffer);
//                }
//                // Read the response back from the server
//                Response response = getAnswer();
//                if (response == null) continue;
//                else if (!response.isSuccessful()) console.printError(response.message());
//                else console.println(response.message());
//
////                if (response != null) console.println(response.message());
////                else throw new IOException();
//                sendStatus = true;
//                return response.isSuccessful();
//            } catch (IOException e) {
////                console.printError("Unable to connect to server. We will try to reconnect in 5 seconds...");
//                if (!errorThrown) {
//                    console.printError("Unable to connect to server. We will try to reconnect...");
//                    errorThrown = true;
//                }
////                try {
////                    Thread.sleep(5000);
////                    start();
//                boolean reconnected = false;
//                try {
//                    while (!reconnected) {
//                        client = SocketChannel.open(address);
//                        client.configureBlocking(false);
//                        reconnected = true;
//                    }
//                } catch (IOException ignored) { }
////                console.println("Try again");
////                console.println("Connection restored!");
////                    sendRequest(request);
////                } catch (InterruptedException ex) {
////                    Thread.currentThread().interrupt();
////                }
//            }
//        } while (!sendStatus);
//        return false;
//    }

    public boolean sendRequest(Sendable request) {
        boolean sendStatus = false;
        do {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
                outputStream.writeObject(request);
                outputStream.close();

                ByteBuffer buffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
                while (buffer.hasRemaining()) {
                    client.write(buffer);
                }

                Response response = getAnswer();
                if (response == null) continue;
//                if (response == null) return false;
                else if (!response.isSuccessful()) {
                    console.printError(response.message());
                } else {
                    console.println(response.message());
                }

                sendStatus = true;
                return response.isSuccessful();
            } catch (IOException e) {
                console.printError("Unable to connect to server. We will try to reconnect...");
                reconnect();
            }
        } while (!sendStatus);
        return false;
    }

    private void reconnect() {
        boolean reconnected = false;
        while (!reconnected) {
            try {
                client = SocketChannel.open(address);
                client.configureBlocking(false);
                reconnected = true;
            } catch (IOException ignored) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }


    private Response getAnswer() throws IOException {
        Selector selector = Selector.open();
        client.register(selector, SelectionKey.OP_READ);


        ByteBuffer buffer = ByteBuffer.allocate(1024);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < 10000) {
            int readyChannels = selector.select(1000); // Таймаут для select, чтобы не блокировать навсегда

            if (readyChannels == 0) {
                continue;
            }

            while (true) {
                int bytesRead = client.read(buffer);
                if (bytesRead == -1) {
                    break; // Конец потока данных
                }
                if (bytesRead == 0) {
                    break; // Нет доступных данных для чтения
                }

                buffer.flip();
                byteArrayOutputStream.write(buffer.array(), 0, buffer.limit());
                buffer.clear();
            }

            byte[] responseBytes = byteArrayOutputStream.toByteArray();
            if (responseBytes.length > 0) {
                try (ObjectInputStream oi = new ObjectInputStream(new ByteArrayInputStream(responseBytes))) {
                    Response response = (Response) oi.readObject();
                    return response;
                } catch (EOFException | StreamCorruptedException ignored) {
                    // Error while deserializing object. Maybe, some data wasn't gotten
                    // Continue reading data...
                } catch (ClassNotFoundException e) {
//                    System.err.println("Error while reading object :" + e.getMessage());
                    console.printError("Error while reading object :" + e.getMessage());
                }
            }
        }
        return null;
    }

}
