package ru.ifmo.ctddev.Nemchenko.HelloUDP;

import java.io.IOException;
import java.net.*;

/**
 * Created by eugene on 2015/05/05.
 */
public class HelloUDPClient {
    private static  String serverId;
    private static  int portNumber;
    public static String prefix;
    public static int cntThreads;
    public static int cntQueries;

    public static void main(String...args) {
        if (args.length < 5) {
            System.out.println("usage: name/ip of server, port number, prefix, count of threads, count of queries");
            return;
        }
        serverId = args[0];
        portNumber = Integer.parseInt(args[1]);
        prefix = args[2];
        cntThreads = Integer.parseInt(args[3]);
        cntQueries = Integer.parseInt(args[4]);

        startSending();
    }

    private static void startSending() {
        byte[] buf = new byte[100];
        for (int i = 0; i < prefix.length(); i++) {
            buf[i] = (byte) prefix.charAt(i);
        }
        InetAddress serverInet = null;
        try {
            serverInet = InetAddress.getByName(serverId);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        DatagramPacket packet = new DatagramPacket(buf, 0, prefix.length(), serverInet, portNumber);

        try {
            DatagramSocket clientSocket = new DatagramSocket();
            clientSocket.send(packet);
            clientSocket.setSoTimeout(2000);
            clientSocket.receive(packet);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
