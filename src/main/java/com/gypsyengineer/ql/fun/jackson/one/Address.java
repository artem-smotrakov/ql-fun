package com.gypsyengineer.ql.fun.jackson.one;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class Address {

    public int postcode;
    public String street;
}
