package com.gypsyengineer.ql.fun.jackson.one;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SerializePerson {

    public static void main(String[] args) throws Exception {
        serialize();
    }

    public static void serialize() throws JsonProcessingException {
        Person person = new Person();
        person.age = 101;
        person.name = "John Doe";
        person.phone = new DomesticNumber();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(person);

        System.out.println(json);
    }
}
