package com.ren1kron.server.network;

import com.ren1kron.common.console.Console;
import com.ren1kron.common.network.Request;
import com.ren1kron.common.network.abstractions.Sendable;
import com.ren1kron.common.network.requestDecorators.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ren1kron.server.managers.CommandManager;
import com.ren1kron.server.utility.Executor;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class TcpServerManager {
    private static final Logger logger = LogManager.getLogger(TcpServerManager.class);
    private InetSocketAddress address;
    private Selector selector;
    private Executor executor;
    private Console console;
    private ExecutorService readThreadPool = Executors.newFixedThreadPool(10); // Fixed thread pool with 10 threads

    public TcpServerManager(InetSocketAddress address, Executor executor, Console console) {
        this.address = address;
        this.executor = executor;
        this.console = console;
    }

    public void start() {
        try {
            this.selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(this.address);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            logger.info("––– Server started on port: " + address.getPort() + " –––");

            new Thread(() -> {
                while (true) {
                    try {
                        CommandManager commandManager = executor.getCommandManager();
                        String input = console.readln().trim();

                        if (input.equals("exit")) {
                            commandManager.getCommands().get("exit").apply(new Request(""));
                        } else {
                            logger.warn("Inserted command is not available for server. You are able to use this commands: 'exit'");
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();

            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (!key.isValid()) continue;
                    if (key.isAcceptable()) handleAccept(key);
                    else if (key.isReadable()) readThreadPool.submit(() -> handleRead(key));
                }
            }

        } catch (IOException e) {
            logger.error("Error in server (while opening selector): ", e);
        }
    }

    private void handleAccept(SelectionKey key) {
        try {
            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            SocketChannel client = channel.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
            logger.info("New client connected: " + client.socket().getRemoteSocketAddress().toString() + "\n");
        } catch (IOException e) {
            logger.error("Error in server while accepting client: " + e.getMessage());
        }
    }

//    private void handleRead(SelectionKey key) {
//        try {
//            SocketChannel client = (SocketChannel) key.channel();
//            client.configureBlocking(false);
//            ByteBuffer buffer = ByteBuffer.allocate(2048);
//            int read = client.read(buffer);
//            if (read == -1) {
//                client.close();
//                logger.info("Client " + client.socket().getRemoteSocketAddress() + " disconnected...");
//                return;
//            }
////            buffer.flip();
////            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));
////            Sendable request = (Sendable) objectInputStream.readObject();
////
////            ForkJoinPool.commonPool().execute(() -> {
////                Response execute = executor.execute(request);
////                new Thread(() -> sendResponse(client, execute)).start();
////            });
//            buffer.flip();
//            byte[] data = new byte[buffer.remaining()];
//            buffer.get(data);
//
//            ForkJoinPool.commonPool().execute(() -> {
//                try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data))) {
//                    Sendable request = (Sendable) objectInputStream.readObject();
//                    Response response = executor.execute(request);
//                    new Thread(() -> sendResponse(client, response)).start();
//                } catch (IOException | ClassNotFoundException e) {
//                    logger.error("Error in server (while reading key): ", e);
//                    e.printStackTrace();
//                }
//            });
//
////        } catch (IOException | ClassNotFoundException e) {
//        } catch (IOException e) {
//            logger.error("Error in server (while reading key): " + e.getMessage());
////            e.printStackTrace();
//        }
//    }
//
//    private void sendResponse(SocketChannel client, Response response) {
//        try {
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            try (ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream)) {
//                outputStream.writeObject(response);
//            }
////            outputStream.close();
//            byte[] data = byteArrayOutputStream.toByteArray();
//            ByteBuffer output = ByteBuffer.wrap(data);
//            client.write(output);
//        } catch (IOException e) {
//            logger.error("Error in server (while sending response): " + e.getMessage());
//        }
//    }

    private void handleRead(SelectionKey key) {
        try {
            SocketChannel client = (SocketChannel) key.channel();
            client.configureBlocking(false);
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            int bytesRead = client.read(buffer);
            if (bytesRead == -1) {
                client.close();
                logger.info("Client " + client.socket().getRemoteSocketAddress() + " disconnected...");
                return;
            }

            while (bytesRead > 0) {
                buffer.flip();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);
                handleRequest(data, client);
                buffer.clear();
                bytesRead = client.read(buffer);
            }
        } catch (IOException e) {
            logger.error("Error in server (while reading key): " + e.getMessage());
        }
    }

    private void handleRequest(byte[] data, SocketChannel client) {
        ForkJoinPool.commonPool().execute(() -> {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data))) {
                Sendable request = (Sendable) objectInputStream.readObject();
                Response response = executor.execute(request);
                new Thread(() -> sendResponse(client, response)).start();
            } catch (IOException | ClassNotFoundException e) {
                logger.error("Error in server (while reading request): " + e.getMessage(), e);
            }
        });
    }

    private void sendResponse(SocketChannel client, Response response) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream)) {
                outputStream.writeObject(response);
            }
            byte[] data = byteArrayOutputStream.toByteArray();
            ByteBuffer output = ByteBuffer.wrap(data);
            client.write(output);
        } catch (IOException e) {
            logger.error("Error in server (while sending response): " + e.getMessage());
        }
    }

}
