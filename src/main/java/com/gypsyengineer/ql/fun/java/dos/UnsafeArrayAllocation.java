package com.gypsyengineer.ql.fun.java.dos;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Shorts;
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

    // BAD: array size comes from remote user input
    public void unsafeArrayAllocation(Socket socket) throws IOException {
        try (InputStream is = socket.getInputStream()) {
            byte[] data = new byte[16];
            int n = is.read(data);

            String[] arr1 = new String[data[0] * data[1]];

            DataInputStream dis = new DataInputStream(is);
            int[] arr2 = new int[dis.readShort()];
            int[] arr3 = new int[dis.readInt()];

            String s = new String(data);
            long[] arr4 = new long[Short.parseShort(s)];
            long[] arr5 = new long[Short.valueOf(s)];
            long[] arr6 = new long[Integer.parseInt(s)];
            long[] arr7 = new long[Integer.valueOf(s)];

            Object[] arr8 = new Object[new BigInteger(data).shortValue()];
            Object[] arr9 = new Object[new BigInteger(data).shortValueExact()];
            Object[] arr10 = new Object[new BigInteger(1, data).intValue()];
            Object[] arr11 = new Object[new BigInteger(1, data).intValueExact()];

            String[][] arr12 = new String[10][NumberUtils.createNumber(s).intValue()];
            String[][] arr13 = new String[10][NumberUtils.createInteger(s).intValue()];
            String[][] arr14 = new String[10][NumberUtils.toShort(s)];
            String[][] arr15 = new String[10][NumberUtils.toInt(s)];

            String[] arr16 = new String[Ints.fromByteArray(data)];
            String[] arr17 = new String[Ints.fromBytes(data[0], data[1], data[2], data[3])];
            String[] arr18 = new String[Ints.tryParse(s)];
            String[] arr19 = new String[Shorts.fromByteArray(data)];
            String[] arr20 = new String[Shorts.fromBytes(data[0], data[1])];

            byte[] arr21 = new byte[ByteBuffer.wrap(data).getShort()];
            byte[] arr22 = new byte[ByteBuffer.wrap(data).getInt()];
        }
    }

    // BAD: size of byte buffer comes from remote user input
    public void unsafeByteBufferAllocation(Socket socket) throws IOException {
        try (InputStream is = socket.getInputStream()) {
            byte[] data = new byte[16];
            int n = is.read(data);
            int size = ByteBuffer.wrap(data).getInt();
            ByteBuffer.allocate(size);
            ByteBuffer.allocateDirect(size);
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

    // BAD: list capacity comes from remote user input
    public void unsafeCollectionAllocation(Socket socket) throws IOException {
        try (InputStream is = socket.getInputStream()) {
            byte[] data = new byte[16];
            is.read(data);
            int size = ByteBuffer.wrap(data).getInt();

            List<Object> list1 = new ArrayList<>(size);
            List<Object> list2 = new Vector<>(size);
            Set<Object> set1 = new HashSet<>(size);
            Map<Object, Object> map1 = new HashMap<>(size);
            Map<Object, Object> map2 = new ConcurrentHashMap<>(size);
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
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(new ArrayList<>(Integer.MAX_VALUE));
        }
    }
}
