package com.gypsyengineer.ql.fun.jackson.two;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

import java.io.IOException;

import static com.gypsyengineer.ql.fun.Util.withSocket;

public class SafeCatDeserialization {

    private static final String command =
            System.getProperty("os.name").toLowerCase().contains("windows")
                    ? "calc.exe" : "gedit";

    private static final String bad =
            "{\"name\":\"Dude\",\"tag\":[" +
                    "\"com.popular.lib.Exec\"," +
                    "{\"command\":\"" + command + "\"}]}";

    private static <T> T deserializeSafe(String string, Class<T> clazz) throws IOException {
        PolymorphicTypeValidator ptv =
                BasicPolymorphicTypeValidator.builder()
                        .allowIfSubType("com.gypsyengineer")
                        .build();
        ObjectMapper mapper = JsonMapper.builder()
                .polymorphicTypeValidator(ptv)
                .build();

        // this enables polymorphic type handling
        mapper.enableDefaultTyping();

        return mapper.readValue(string, clazz);
    }

    public static void main(String[] args) throws Exception {
        try {
            deserializeSafe(bad, Cat.class);
        } catch (Exception e) {
            System.out.println("Deserialization failed as expected: " + e);
        }
    }

    // tests for CodeQL

    // GOOD
    private static void testUnsafeDeserialization() throws Exception {
        withSocket(input -> deserializeSafe(input, Cat.class));
    }
}
