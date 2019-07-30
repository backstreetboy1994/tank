package com.mashibing.net.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss =new ServerSocket();
        ss.bind(new InetSocketAddress("localhost",8888));
        boolean started = true;

        while (started) {
            Socket s = ss.accept();
            new Thread(() -> {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String str = br.readLine();
                    System.out.println(str);
                    br.close();
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        ss.close();
    }
}
