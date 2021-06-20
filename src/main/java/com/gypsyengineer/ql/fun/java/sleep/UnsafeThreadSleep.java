package com.gypsyengineer.ql.fun.java.sleep;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class UnsafeThreadSleep {

    public static void unsafeSleep(Socket socket) throws IOException, InterruptedException {
        InputStream is = socket.getInputStream();
        byte[] bytes = new byte[4];
        is.read(bytes);
        String s = new String(bytes);

        // with multiplication and casting
        int delay = Integer.parseInt(s);
        Thread.sleep(delay);
        Thread.sleep((long) delay * 1000);
        Thread.sleep(delay * 1000L);

        Thread.sleep(new Integer(s));
        Thread.sleep(new Long(s));

        Thread.sleep(Integer.valueOf(s));
        Thread.sleep(Integer.parseInt(s));
        Thread.sleep(Integer.parseUnsignedInt(s));
        Thread.sleep(Integer.decode(s));

        Thread.sleep(Long.parseLong(s));
        Thread.sleep(Long.parseUnsignedLong(s));
        Thread.sleep(Long.valueOf(s));
        Thread.sleep(Long.decode(s));

        // Apache Commons
        Thread.sleep(NumberUtils.createInteger(s));
        Thread.sleep(NumberUtils.createLong(s));
        Thread.sleep(NumberUtils.toInt(s));
        Thread.sleep(NumberUtils.toLong(s));

        // Guava
        Thread.sleep(Ints.tryParse(s));
        Thread.sleep(Ints.fromByteArray(bytes));
        Thread.sleep(Longs.tryParse(s));
        Thread.sleep(Longs.fromByteArray(bytes));
    }

}
