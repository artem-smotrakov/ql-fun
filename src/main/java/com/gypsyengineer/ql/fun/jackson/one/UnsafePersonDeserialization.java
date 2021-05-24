package com.gypsyengineer.ql.fun.jackson.one;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static com.gypsyengineer.ql.fun.Util.withSocket;

public class UnsafePersonDeserialization {

    private static final String COMMAND =
            System.getProperty("os.name").toLowerCase().contains("windows")
                    ? "calc.exe" : "gedit";

    private static final String BAD_PERSON =
            "{\"name\":\"Bender\","
                    + "\"age\":101,"
                    + "\"phone\":{"
                    + "   \"@class\":\"com.popular.lib.Exec\","
                    + "   \"command\":\"" + COMMAND + "\""
                    + "}}";

    private static final String BAD_TASK = String.format("{ \"assignee\": %s }", BAD_PERSON);

    private static Person deserializePersonUnsafe(String string) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(string, Person.class);
    }

    private static Employee deserializeEmployeeUnsafe(String string) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(string, Employee.class);
    }

    private static Task deserializeTaskUnsafe(String string) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(string, Task.class);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(deserializeTaskUnsafe(BAD_TASK));
    }

    // tests for CodeQL

    // BAD
    private static void testUnsafeDeserialization() throws Exception {
        withSocket(UnsafePersonDeserialization::deserializePersonUnsafe);
    }

    // BAD
    private static void testUnsafeDeserializationWithExtendedClass() throws Exception {
        withSocket(UnsafePersonDeserialization::deserializeEmployeeUnsafe);
    }

    // BAD
    private static void testUnsafeDeserializationWithWrapper() throws Exception {
        withSocket(UnsafePersonDeserialization::deserializeTaskUnsafe);
    }
}
