package com.gypsyengineer.ql.fun.jackson.two;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

import java.io.IOException;

import static com.gypsyengineer.ql.fun.Util.withSocket;

public class SaferCatDeserialization {

    private static final String COMMAND =
            System.getProperty("os.name").toLowerCase().contains("windows")
                    ? "calc.exe" : "gedit";

    private static final String GOOD_CAT =
            "{\"name\":\"Dude\"," +
                    "\"tag\":[" +
                    "   \"com.gypsyengineer.ql.fun.jackson.two.Tag\"," +
                    "   {\"title\":\"123\"}" +
                    "]}";

    private static final String BAD_CAT =
            "{\"name\":\"Dude\"," +
                    "\"tag\":[" +
                    "   \"com.popular.lib.Exec\"," +
                    "   {\"command\":\"" + COMMAND + "\"}" +
                    "]}";

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
        System.out.println(deserializeSafe(GOOD_CAT, Cat.class));

        try {
            System.out.println(deserializeSafe(BAD_CAT, Cat.class));
        } catch (Exception e) {
            System.out.println("Deserialization failed: " + e);
        }
    }

    // tests for CodeQL

    // GOOD
    private static void testUnsafeDeserialization() throws Exception {
        withSocket(input -> deserializeSafe(input, Cat.class));
    }
}
