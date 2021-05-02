package com.popular.lib;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class Exec implements Serializable {

    private String command;
    private String output;

    public Exec() {

    }

    public void setCommand(String command) throws IOException, InterruptedException {
        this.command = command;
        byte[] bytes = new byte[1024];
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor(1, TimeUnit.SECONDS);
        int n = process.getInputStream().read(bytes);
        if (n > 0) {
            this.output = new String(bytes, 0, n);
        }
    }

    public String getOutput() {
        return output;
    }

}
