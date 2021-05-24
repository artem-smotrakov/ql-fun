package com.gypsyengineer.ql.fun.jackson.one;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SafeHouseDeserialization {

    private static final String COMMAND =
            System.getProperty("os.name").toLowerCase().contains("windows")
                    ? "calc.exe" : "gedit";

    private static final String BAD_HOUSE =
            "{\"address\":{"
                    + "   \"@class\":\"com.popular.lib.Exec\","
                    + "   \"command\":\"" + COMMAND + "\""
                    + "}}";

    // the attack does not work because a gadget has to extend Address
    private static House deserializeHouseSafe(String string) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(string, House.class);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(deserializeHouseSafe(BAD_HOUSE));
    }
}
