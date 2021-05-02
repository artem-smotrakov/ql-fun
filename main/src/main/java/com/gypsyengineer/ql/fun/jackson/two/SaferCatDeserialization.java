package com.gypsyengineer.ql.fun.jackson.two;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

public class SaferCatDeserialization {

    private static final String command =
            System.getProperty("os.name").toLowerCase().contains("windows")
                    ? "calc.exe" : "gedit";

    private static final String good =
            "{\"name\":\"Dude\"," +
                    "\"tag\":[" +
                    "   \"com.gypsyengineer.jackson.unsafe.two.Tag\"," +
                    "   {\"title\":\"123\"}" +
                    "]}";

    private static final String bad =
            "{\"name\":\"Dude\"," +
                    "\"tag\":[" +
                    "   \"com.pupolar.lib.Exec\"," +
                    "   {\"command\":\"" + command + "\"}" +
                    "]}";

    public static void main(String[] args) throws Exception {
        PolymorphicTypeValidator ptv =
                BasicPolymorphicTypeValidator.builder()
                        .allowIfSubType("com.gypsyengineer.jackson")
                        .build();
        ObjectMapper mapper = JsonMapper.builder()
                .activateDefaultTyping(ptv)
                .build();

        Cat cat = mapper.readValue(good, Cat.class);
        System.out.println(cat.toString());

        try {
            mapper.readValue(bad, Cat.class);
        } catch (Exception e) {
            System.out.println("Deserialization failed: " + e);
        }
    }
}
