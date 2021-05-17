package com.gypsyengineer.ql.fun.jackson.two;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import static com.gypsyengineer.ql.fun.Util.withSocket;

public class UnsafeCatDeserialization {

    private static final String COMMAND =
            System.getProperty("os.name").toLowerCase().contains("windows")
                    ? "calc.exe" : "gedit";

    private static final String BAD_CAT =
            "{\"name\":\"Dude\",\"tag\":[" +
                    "\"com.popular.lib.Exec\"," +
                    "{\"command\":\"" + COMMAND + "\"}]}";

    private static <T> T deserializeUnsafe(String string, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // this enables polymorphic type handling
        mapper.enableDefaultTyping();

        return mapper.readValue(string, clazz);
    }

    private static <T> List<T> deserializeUnsafeWithMappingIterator(String string, Class<T> clazz)
            throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();
        return mapper.readValues(new JsonFactory().createParser(string), clazz).readAll();
    }

    private static <T> T deserializeUnsafeWithTreeToValue(String string, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // this enables polymorphic type handling
        mapper.enableDefaultTyping();

        return mapper.treeToValue(mapper.readTree(string), clazz);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(deserializeUnsafeWithTreeToValue(BAD_CAT, Cat.class));
    }

    // tests for CodeQL

    // BAD
    private static void testUnsafeDeserialization() throws Exception {
        withSocket(input -> deserializeUnsafe(input, Cat.class));
    }

    // BAD
    private static void testUnsafeDeserializationWithObjectMapperReadValues() throws Exception {
        withSocket(input -> deserializeUnsafeWithMappingIterator(input, Cat.class));
    }

    // BAD
    private static void testUnsafeDeserializationWithObjectMapperTreeToValue() throws Exception {
        withSocket(input -> deserializeUnsafeWithTreeToValue(input, Cat.class));
    }

    // BAD
    private static void testUnsafeDeserializationWithUnsafeClass() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            try (Socket socket = serverSocket.accept()) {
                byte[] bytes = new byte[1024];
                int n = socket.getInputStream().read(bytes);
                String data = new String(bytes, 0, n);
                String unsafeType = new String(bytes, 800, n);
                Class unsafeClass = Class.forName(unsafeType);
                ObjectMapper mapper = new ObjectMapper();
                mapper.readValue(data, unsafeClass);
            }
        }
    }
}
