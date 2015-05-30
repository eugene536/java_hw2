package ru.ifmo.ctddev.Nemchenko.HelloUDP;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class for running servers which listen on port passed in command line arguments
 * and response on all requests "Hello, request"
 */
public class HelloUDPServer implements HelloServer {
    private static final int BUF_SIZE = 100;
    private static final int TIMEOUT = 10;
    private static ArrayList<ExecutorService> workingThreads = new ArrayList<>();
    private static ArrayList<DatagramSocket> serverSockets = new ArrayList<>();
    private static final Charset CHARSET = Charset.forName("utf-8");

    public static void main(String... args) {
        if (args.length < 2) {
            System.out.println("usage: port number, count of working threads");
            return;
        }
        int portNumber = Integer.parseInt(args[0]);
        int cntWorkingThreads = Integer.parseInt(args[1]);
        new HelloUDPServer().start(portNumber, cntWorkingThreads);
    }

    /**
     * create server on port {@code port} and receive requests in {@code threads} parallel running threads.
     *
     * @param port    target port
     * @param threads count of threads, which will be used for receiving requests
     */
    @Override
    public void start(int port, int threads) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(port);
            workingThreads.add(Executors.newFixedThreadPool(threads));

            for (int i = 0; i < threads; i++) {
                workingThreads.get(workingThreads.size() - 1).execute(new ReceiveTask(serverSocket));
            }

        } catch (SocketException e) { // TBD
            System.out.println("can't bind on port: " + port);
        }
    }

    /**
     * close all working threads and binding ports
     */
    @Override
    public void close() {
        workingThreads.forEach(java.util.concurrent.ExecutorService::shutdownNow);
        serverSockets.forEach(java.net.DatagramSocket::close);
    }

    private class ReceiveTask implements Runnable {
        private DatagramSocket serverSocket;

        public ReceiveTask(DatagramSocket serverSocket) {
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            DatagramPacket packet = new DatagramPacket(new byte[BUF_SIZE], 0, BUF_SIZE);
            while (!Thread.interrupted()) {
                try {
                    serverSocket.setSoTimeout(TIMEOUT);
                    serverSocket.receive(packet);

                    String request = new String(packet.getData(), packet.getOffset(), packet.getLength(), CHARSET);
                    String response = "Hello, " + request;
                    DatagramPacket sendPacket = new DatagramPacket(response.getBytes(), 0, response.length(),
                            packet.getAddress(), packet.getPort());
                    serverSocket.send(sendPacket);
                } catch (SocketTimeoutException e) {
                    // ignore
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
