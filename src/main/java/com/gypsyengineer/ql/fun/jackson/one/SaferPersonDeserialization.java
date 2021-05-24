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

    private static final String GOOD_PERSON =
            "{\"name\":\"John Doe\"," +
                    "\"age\":101," +
                    "\"phone\":{" +
                    "   \"@class\":\"com.gypsyengineer.ql.fun.jackson.one.DomesticNumber\"," +
                    "   \"areaCode\":0," +
                    "   \"local\":0}}";

    private static final String BAD_PERSON =
            "{\"name\":\"Bender\","
                    + "\"age\":101,"
                    + "\"phone\":{"
                    + "   \"@class\":\"com.popular.lib.Exec\","
                    + "   \"command\":\"" + command + "\""
                    + "}}";

    private static Person deserializePersonSafeWithValidatorAndBuilder(String string) throws IOException {
        PolymorphicTypeValidator ptv =
                BasicPolymorphicTypeValidator.builder()
                        .allowIfSubType("com.gypsyengineer")
                        .build();
        ObjectMapper mapper = JsonMapper.builder()
                .polymorphicTypeValidator(ptv)
                .build();

        return mapper.readValue(string, Person.class);
    }

    private static Person deserializePersonSafeWithValidator(String string) throws IOException {
        PolymorphicTypeValidator ptv =
                BasicPolymorphicTypeValidator.builder()
                        .allowIfSubType("com.gypsyengineer")
                        .build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPolymorphicTypeValidator(ptv);

        return mapper.readValue(string, Person.class);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(deserializePersonSafeWithValidatorAndBuilder(GOOD_PERSON));

        try {
            deserializePersonSafeWithValidatorAndBuilder(BAD_PERSON);
        } catch (Exception e) {
            System.out.println("Deserialization failed as expected: " + e);
        }
    }

    // tests for CodeQL

    // GOOD
    private static void testSafeDeserializationWithValidator() throws Exception {
        withSocket(SaferPersonDeserialization::deserializePersonSafeWithValidator);
    }

    // GOOD
    private static void testSafeDeserializationWithValidatorAndBuilder() throws Exception {
        withSocket(SaferPersonDeserialization::deserializePersonSafeWithValidatorAndBuilder);
    }
}
