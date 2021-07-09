package com.gypsyengineer.ql.fun.java.dos;

import com.google.common.primitives.Ints;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UnsafeArrayAllocation {

    // below are tests for CodeQL queries

    private static final int MAX_SIZE = 100;

    public void unsafeArrayAllocation(Socket socket) throws IOException {
        try (InputStream is = socket.getInputStream()) {
            byte[] data = new byte[16];
            int n = is.read(data);

            String[] arr1 = new String[data[0] * data[1]]; // BAD

            DataInputStream dis = new DataInputStream(is);
            int[] arr2 = new int[dis.readInt()]; // BAD

            String s = new String(data);
            long[] arr3 = new long[Integer.parseInt(s)]; // BAD;
            long[] arr4 = new long[Integer.valueOf(s)]; // BAD

            Object[] arr5 = new Object[new BigInteger(1, data).intValue()]; // BAD
            Object[] arr6 = new Object[new BigInteger(1, data).intValueExact()]; // BAD

            String[][] arr7 = new String[10][NumberUtils.createNumber(s).intValue()]; // BAD
            String[][] arr8 = new String[10][NumberUtils.createInteger(s).intValue()]; // BAD
            String[][] arr9 = new String[10][NumberUtils.toInt(s)]; // BAD

            String[] arr10 = new String[Ints.fromByteArray(data)]; // BAD
            String[] arr11 = new String[Ints.fromBytes(data[0], data[1], data[2], data[3])]; // BAD
            String[] arr12 = new String[Ints.tryParse(s)]; // BAD

            byte[] arr13 = new byte[ByteBuffer.wrap(data).getInt()]; // BAD
        }
    }

    public void unsafeByteBufferAllocation(Socket socket) throws IOException {
        try (InputStream is = socket.getInputStream()) {
            byte[] data = new byte[16];
            int n = is.read(data);
            int size = ByteBuffer.wrap(data).getInt();
            ByteBuffer.allocate(size); // BAD
            ByteBuffer.allocateDirect(size); // BAD
        }
    }

    // GOOD: size of byte buffer comes from remote user input, but it is sanitized with Math.min()
    public void safeByteBufferAllocationWithMathMin(Socket socket) throws IOException {
        try (InputStream is = socket.getInputStream()) {
            byte[] data = new byte[16];
            int n = is.read(data);
            int size = Math.min(ByteBuffer.wrap(data).getInt(), MAX_SIZE);
            ByteBuffer.allocate(size);
        }
    }

    // GOOD: size of byte buffer comes from remote user input, but it is sanitized with a custom check
    public void safeByteBufferAllocationWithCustomCheckOne(Socket socket) throws IOException {
        try (InputStream is = socket.getInputStream()) {
            byte[] data = new byte[16];
            int n = is.read(data);
            int size = ByteBuffer.wrap(data).getInt();
            if (size > MAX_SIZE) {
                throw new IllegalArgumentException("Too much!");
            }
            ByteBuffer.allocate(size);
        }
    }

    // GOOD: size of byte buffer comes from remote user input, but it is sanitized with a custom check
    public void safeByteBufferAllocationWithCustomCheckTwo(Socket socket) throws IOException {
        try (InputStream is = socket.getInputStream()) {
            byte[] data = new byte[16];
            int n = is.read(data);
            int size = ByteBuffer.wrap(data).getInt();
            if (MAX_SIZE < size) {
                return;
            }
            ByteBuffer.allocate(size);
        }
    }

    public void unsafeCollectionAllocation(Socket socket) throws IOException {
        try (InputStream is = socket.getInputStream()) {
            byte[] data = new byte[16];
            is.read(data);
            int size = ByteBuffer.wrap(data).getInt();

            List<Object> list1 = new ArrayList<>(size); // BAD
            List<Object> list2 = new Vector<>(size); // BAD
            Set<Object> set1 = new HashSet<>(size); // BAD
            Map<Object, Object> map1 = new HashMap<>(size); // BAD
            Map<Object, Object> map2 = new ConcurrentHashMap<>(size); // BAD
        }
    }

    // BAD: number of copies comes from remote user input
    public void unsafeCopy(Socket socket) throws IOException {
        try (InputStream is = socket.getInputStream()) {
            byte[] data = new byte[16];
            is.read(data);
            int n = ByteBuffer.wrap(data).getInt();

            Collections.nCopies(n, new Object());
        }
    }

    public static void main(String... args) {
        int n = (read() << 24) + (read() << 16) + (read() << 8) + read();
        ByteBuffer.allocate(n);
    }

    public static int read() {
        return 0b01111111;
    }
}
