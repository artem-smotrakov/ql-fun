package com.gypsyengineer.ql.fun.java.deserialization;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class UnsafeObjectInputStream {

    public static void main(String... args) throws IOException, ClassNotFoundException {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            Socket socket = serverSocket.accept();
            try (InputStream is = new BufferedInputStream(socket.getInputStream());
                 ObjectInputStream ois = new ObjectInputStream(is)) {
                Object object = ois.readObject();
                System.out.println(object);
            }
        }
    }
}
