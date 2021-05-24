package com.gypsyengineer.ql.fun.jackson.two;

import java.io.Serializable;

public class Cat {

    private String name;
    private Serializable tag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Serializable tag) {
        this.tag = tag;
    }
}
