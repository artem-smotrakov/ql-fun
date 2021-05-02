package com.gypsyengineer.ql.fun.jackson.two;

import java.io.Serializable;

public class Tag implements Serializable {

    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
