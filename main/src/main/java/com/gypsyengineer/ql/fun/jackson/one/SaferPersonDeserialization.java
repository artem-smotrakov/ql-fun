package com.gypsyengineer.ql.fun.jackson.one;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

import java.io.IOException;

import static com.gypsyengineer.ql.fun.Util.withSocket;

public class SaferPersonDeserialization {

    private static final String command =
            System.getProperty("os.name").toLowerCase().contains("windows")
                    ? "calc.exe" : "gedit";

    private static final String good =
            "{\"name\":\"John Doe\"," +
                    "\"age\":101," +
                    "\"phone\":{" +
                    "   \"@class\":\"com.gypsyengineer.ql.fun.jackson.one.DomesticNumber\"," +
                    "   \"areaCode\":0," +
                    "   \"local\":0}}";

    private static final String bad =
            "{\"name\":\"Bender\","
                    + "\"age\":101,"
                    + "\"phone\":{"
                    + "   \"@class\":\"com.popular.lib.Exec\","
                    + "   \"command\":\"" + command + "\""
                    + "}}";

    private static <T> T deserializeSafe(String string, Class<T> clazz) throws IOException {
        PolymorphicTypeValidator ptv =
                BasicPolymorphicTypeValidator.builder()
                        .allowIfSubType("com.gypsyengineer")
                        .build();
        ObjectMapper mapper = JsonMapper.builder()
                .polymorphicTypeValidator(ptv)
                .build();

        return mapper.readValue(string, clazz);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(deserializeSafe(good, Person.class));

        try {
            deserializeSafe(bad, Person.class);
        } catch (Exception e) {
            System.out.println("Deserialization failed as expected: " + e);
        }
    }

    // tests for CodeQL

    // GOOD
    private static void testSafeDeserialization() throws Exception {
        withSocket(input -> deserializeSafe(input, Person.class));
    }
}
