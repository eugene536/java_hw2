package ru.ifmo.ctddev.Nemchenko.HelloUDP;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * class for sending requests in parallel threads, see description of {@link #start(String, int, String, int, int)}
 */
public class HelloUDPClient implements HelloClient {
    private static final int TIMEOUT = 100;
    private static final Charset CHARSET = Charset.forName("utf-8");

    public static void main(String... args) {
        if (args.length < 5) {
            System.out.println("usage: name/ip of server, port number, prefix, count of threads, count of queries");
            return;
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String prefix = args[2];
        int threads = Integer.parseInt(args[3]);
        int requests = Integer.parseInt(args[4]);

        new HelloUDPClient().start(host, port, prefix, requests, threads);
    }

    /**
     * send {@code requests} requests in parallel {@code threads} threads on host {@code host} in port {@code port}
     * requests are generated like "{@code prefix}n_m",
     * where n - number of current thread, m - number of current request
     * @param host server host
     * @param port target port
     * @param prefix for generated request
     * @param requests count of requests which will be sent on server host
     * @param threads count of threads which will be send a requests on server host
     */
    @Override
    public void start(String host, int port, String prefix, int requests, int threads) {
        InetAddress serverInet;
        try {
            serverInet = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            System.out.println("unknown host: " + host);
            return;
        }

        ArrayList<Thread> threadsList = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            threadsList.add(new Thread(new SendTask(i, port, serverInet, requests, prefix)));
            threadsList.get(threadsList.size() - 1).start();
        }

        try {
            for (Thread thread : threadsList) {
                thread.join();
            }
        } catch (InterruptedException e) {
            // ignore
        }
    }

    private class SendTask implements Runnable {
        private int threadId;
        private int port;
        private InetAddress serverInet;
        private int requests;
        private String prefix;

        public SendTask(int threadId, int port, InetAddress serverInet, int requests, String prefix) {
            this.threadId = threadId;
            this.port = port;
            this.serverInet = serverInet;
            this.requests = requests;
            this.prefix = prefix;
        }

        @Override
        public void run() {
            int i = 0;
            for (; i < requests; ) {
                try {
                    String request = prefix + threadId + "_" + i;
                    try (DatagramSocket clientSocket = new DatagramSocket()) {
                        DatagramPacket packet = new DatagramPacket(request.getBytes(), request.length(), serverInet, port);
                        clientSocket.send(packet);
                        clientSocket.setSoTimeout(TIMEOUT);

                        byte buf[] = new byte[request.length() + 10];
                        DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
                        clientSocket.receive(receivePacket);

                        String response = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength(), CHARSET);
                        if (("Hello, " + request).equals(response)) {
                            i++;
//                            System.out.println("client: request: " + request);
//                            System.out.println("client: response: " + response);
                        }
                    }
                } catch (SocketTimeoutException e) {
                    // ignoring
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
