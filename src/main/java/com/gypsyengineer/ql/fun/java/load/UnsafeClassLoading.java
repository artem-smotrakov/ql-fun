package com.gypsyengineer.ql.fun.java.load;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class UnsafeClassLoading {

    private static class CustomClassLoader extends ClassLoader {

        public Class<?> load(String name, byte[] bytecode, int off, int len) {
            return defineClass(name, bytecode, off, len);
        }
    }

    public Class<?> unsafeDefineClass(Socket socket) throws IOException {
        try (InputStream is = socket.getInputStream()) {
            byte[] data = new byte[1024];
            int n = is.read(data);
            return new CustomClassLoader().load("RemoteClass", data, 0, n);
        }
    }
}
