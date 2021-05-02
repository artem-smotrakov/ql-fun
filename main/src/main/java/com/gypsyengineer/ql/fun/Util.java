package com.gypsyengineer.ql.fun;

import java.net.ServerSocket;
import java.net.Socket;

public class Util {

    public interface Action<T> {
        void run(T object) throws Exception;
    }

    public static void withSocket(Action<String> action) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            try (Socket socket = serverSocket.accept()) {
                byte[] bytes = new byte[1024];
                int n = socket.getInputStream().read(bytes);
                String jexlExpr = new String(bytes, 0, n);
                action.run(jexlExpr);
            }
        }
    }
}
