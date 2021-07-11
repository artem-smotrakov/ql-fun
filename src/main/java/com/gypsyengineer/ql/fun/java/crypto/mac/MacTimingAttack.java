package com.gypsyengineer.ql.fun.java.crypto.mac;

import javax.crypto.Mac;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

public class MacTimingAttack {

    public boolean unsafeMacCheckWithArrayEquals(Socket socket) throws Exception {
        try (InputStream is = socket.getInputStream()) {
            Mac mac = Mac.getInstance("HmacSHA256");
            byte[] data = new byte[1024];
            is.read(data);
            byte[] actualMac = mac.doFinal(data);
            byte[] expectedMac = is.readNBytes(32);
            return Arrays.equals(expectedMac, actualMac);
        }
    }

    public boolean unsafeMacCheckWithArraysEquals(Socket socket) throws Exception {
        try (InputStream is = socket.getInputStream()) {
            byte[] data = new byte[256];
            byte[] tag = new byte[32];

            is.read(data);
            is.read(tag);

            Mac mac = Mac.getInstance("Hmac256");
            byte[] computedTag = mac.doFinal(data);
            return Arrays.equals(computedTag, tag);
        }
    }

    public boolean unsafeMacCheckWithLoop(Socket socket) throws Exception {
        try (InputStream is = socket.getInputStream()) {
            byte[] data = new byte[256];
            byte[] tag = new byte[32];

            is.read(data);
            is.read(tag);

            Mac mac = Mac.getInstance("Hmac256");
            byte[] computedTag = mac.doFinal(data);

            for (int i = 0; i < computedTag.length; i++) {
                byte a = computedTag[i];
                byte b = tag[i];
                if (a != b) {
                    return false;
                }
            }

            return true;
        }
    }
}
