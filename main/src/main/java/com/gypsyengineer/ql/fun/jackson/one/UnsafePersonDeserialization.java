package com.gypsyengineer.ql.fun.jackson.one;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static com.gypsyengineer.ql.fun.Util.withSocket;

public class UnsafePersonDeserialization {

    private static final String command =
            System.getProperty("os.name").toLowerCase().contains("windows")
                    ? "calc.exe" : "gedit";

    private static final String bad =
            "{\"name\":\"Bender\","
                    + "\"age\":101,"
                    + "\"phone\":{"
                    + "   \"@class\":\"com.popular.lib.Exec\","
                    + "   \"command\":\"" + command + "\""
                    + "}}";

    private static <T> T deserializeUnsafe(String string, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(string, clazz);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(deserializeUnsafe(bad, Person.class));
    }

    // tests for CodeQL

    // BAD
    private static void testUnsafeDeserialization() throws Exception {
        withSocket(input -> deserializeUnsafe(input, Person.class));
    }
}
