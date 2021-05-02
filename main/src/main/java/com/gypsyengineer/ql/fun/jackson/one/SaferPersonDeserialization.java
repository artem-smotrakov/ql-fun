package com.gypsyengineer.ql.fun.jackson.one;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

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

    public static void main(String[] args) throws Exception {
        PolymorphicTypeValidator ptv =
                BasicPolymorphicTypeValidator.builder()
                        .allowIfSubType("com.gypsyengineer.jackson")
                        .build();
        ObjectMapper mapper = JsonMapper.builder()
                .polymorphicTypeValidator(ptv)
                .build();

        Person person = mapper.readValue(good, Person.class);
        System.out.println(person.toString());

        try {
            mapper.readValue(bad, Person.class);
        } catch (Exception e) {
            System.out.println("Deserialization failed: " + e);
        }
    }
}
