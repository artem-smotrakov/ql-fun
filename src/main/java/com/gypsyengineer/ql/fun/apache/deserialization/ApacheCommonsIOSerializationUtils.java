package com.gypsyengineer.ql.fun.apache.deserialization;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.lang3.SerializationUtils;

public class ApacheCommonsIOSerializationUtils {

    public static void main(String... args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            Socket socket = serverSocket.accept();
            try (InputStream is = new BufferedInputStream(socket.getInputStream())) {
                Object object = SerializationUtils.deserialize(is);
                System.out.println(object);
            }
        }
    }
}
