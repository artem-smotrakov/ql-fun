package com.gypsyengineer.ql.fun.jackson.two;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SerializeCat {

    public static void main(String[] args) throws Exception {
        serialize();
    }

    public static void serialize() throws JsonProcessingException {
        Cat cat = new Cat();
        cat.setName("Dude");
        Tag tag = new Tag();
        tag.setTitle("123");
        cat.setTag(tag);

        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();
        String json = mapper.writeValueAsString(cat);

        System.out.println(json);
    }
}
