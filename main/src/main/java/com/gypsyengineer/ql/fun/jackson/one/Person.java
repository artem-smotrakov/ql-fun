package com.gypsyengineer.ql.fun.jackson.one;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

public class Person {

    public String name;
    public int age;

    // Using JsonTypeInfo.Id.CLASS enables polymorphic type handling.
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    public Object phone;

}
