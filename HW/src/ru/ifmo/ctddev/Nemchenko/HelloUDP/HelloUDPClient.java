package ru.ifmo.ctddev.Nemchenko.HelloUDP;

import info.kgeorgiy.java.advanced.hello.HelloClient;
import info.kgeorgiy.java.advanced.hello.Util;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by eugene on 2015/05/05.
 */
public class HelloUDPClient implements HelloClient {
    private ExecutorService executorService;

    public HelloUDPClient() {
        executorService = Executors.newCachedThreadPool();
    }

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

    @Override
    public void start(String host, int port, String prefix, int requests, int threads) {
        InetAddress serverInet = null;
        try {
            serverInet = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            System.out.println("unknown host: " + host);
            return;
        }

        for (int i = 0; i < threads; i++) {
            executorService.submit(new SendTask(i, port, serverInet, requests, prefix));
        }

        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
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
                        clientSocket.setSoTimeout(10);

                        byte buf[] = new byte[request.length() + 20];
                        DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
                        clientSocket.receive(receivePacket);

                        String response = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
                        if (("Hello, " + request).equals(response)) {
                            i++;
                        }
                    }
                } catch (IOException e) {
                    // ignoring
                }
            }
        }
    }

}
