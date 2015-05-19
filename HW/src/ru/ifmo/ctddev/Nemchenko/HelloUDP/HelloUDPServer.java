package ru.ifmo.ctddev.Nemchenko.HelloUDP;

import com.sun.jndi.toolkit.url.Uri;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.*;

/**
 * Created by eugene on 2015/05/05.
 */
public class HelloUDPServer {
    public static int portNumber;
    public static int cntWorkingThreads;
    public static Integer id = 0;

    public static Runnable rs = new Runnable() {
        @Override
        public void run() {
            System.out.println("start thread #" + Thread.currentThread().getName());
            runServer();
        }
    };

    private static DatagramSocket serverSocket;
    private static DatagramSocket clientSocket;

    public static void main(String... args) {
        if (args.length < 2) {
            System.out.println("usage: port number, count of working threads");
            return;
        }
        portNumber = Integer.parseInt(args[0]);
        cntWorkingThreads = Integer.parseInt(args[1]);
        ExecutorService executorService = Executors.newFixedThreadPool(cntWorkingThreads);
        try {
            serverSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private static void runServer() {
        try {
            byte[] buf = new byte[100];
            DatagramPacket packet = new DatagramPacket(buf, 0, buf.length);
            serverSocket.receive(packet);

            System.out.println("receive thread #" + Thread.currentThread().getName());
            System.out.println(packet.getPort());
            for (int i = 0; i < packet.getLength(); i++) {
                System.out.print((char) buf[i]);
            }

            new DatagramSocket(packet.getPort(), packet.getAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
