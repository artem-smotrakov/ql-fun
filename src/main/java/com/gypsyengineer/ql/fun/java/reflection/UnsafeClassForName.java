package com.gypsyengineer.ql.fun.java.reflection;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class UnsafeClassForName {

    public static void main(String... args) throws IOException, ClassNotFoundException {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            Socket socket = serverSocket.accept();
            try (InputStream is = new BufferedInputStream(socket.getInputStream())) {
                byte[] bytes = new byte[1024];
                int n = is.read(bytes);
                Class.forName(new String(bytes, 0, n));
            }
        }
    }
}
