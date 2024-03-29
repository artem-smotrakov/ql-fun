package com.gypsyengineer.ql.fun.java.crypto.cipher;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.SecureRandom;
import java.util.Arrays;

public class StaticInitializationVector {

    // below are tests for CodeQL queries

    // BAD: AES-GCM with static IV from a byte array
    public byte[] encryptWithStaticIvByteArrayWithInitializer(byte[] key, byte[] plaintext) throws Exception {
        byte[] iv = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5 };

        GCMParameterSpec ivSpec = new GCMParameterSpec(128, iv);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        cipher.update(plaintext);
        return cipher.doFinal();
    }

    // BAD: AES-GCM with static IV from zero-initialized byte array
    public byte[] encryptWithZeroStaticIvByteArray(byte[] key, byte[] plaintext) throws Exception {
        byte[] iv = new byte[16];

        GCMParameterSpec ivSpec = new GCMParameterSpec(128, iv);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        cipher.update(plaintext);
        return cipher.doFinal();
    }

    // BAD: AES-CBC with static IV from zero-initialized byte array
    public byte[] encryptWithStaticIvByteArray(byte[] key, byte[] plaintext) throws Exception {
        byte[] iv = new byte[16];
        for (byte i = 0; i < iv.length; i++) {
            iv[i] = 1;
        }

        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        cipher.update(plaintext);
        return cipher.doFinal();
    }

    // BAD: AES-CBC with static IV from a hardcoded string
    public byte[] encryptWithStaticIvFromHardcodedString(byte[] key, byte[] plaintext) throws Exception {
        IvParameterSpec ivSpec = new IvParameterSpec(decode("01234567890123456"));
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        cipher.update(plaintext);
        return cipher.doFinal();
    }

    public byte[] decode(String s) {
        return s.getBytes();
    }

    // GOOD: AES-GCM with a random IV
    public byte[] encryptWithRandomIv(byte[] key, byte[] plaintext) throws Exception {
        byte[] iv = new byte[16];

        SecureRandom random = SecureRandom.getInstanceStrong();
        random.nextBytes(iv);

        GCMParameterSpec ivSpec = new GCMParameterSpec(128, iv);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        cipher.update(plaintext);
        return cipher.doFinal();
    }

    // GOOD: AES-GCM with a random IV
    public byte[] encryptWithRandomIvByteByByte(byte[] key, byte[] plaintext) throws Exception {
        SecureRandom random = SecureRandom.getInstanceStrong();
        byte[] iv = new byte[16];
        for (int i = 0; i < iv.length; i++) {
            iv[i] = (byte) random.nextInt();
        }

        GCMParameterSpec ivSpec = new GCMParameterSpec(128, iv);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        cipher.update(plaintext);
        return cipher.doFinal();
    }

    // GOOD: AES-GCM with a random IV
    public byte[] encryptWithRandomIvWithSystemArrayCopy(byte[] key, byte[] plaintext) throws Exception {
        byte[] randomBytes = new byte[16];
        SecureRandom.getInstanceStrong().nextBytes(randomBytes);

        byte[] iv = new byte[16];
        System.arraycopy(randomBytes, 0, iv, 0, 16);

        GCMParameterSpec ivSpec = new GCMParameterSpec(128, iv);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        cipher.update(plaintext);
        return cipher.doFinal();
    }

    // GOOD: AES-GCM with a random IV
    public byte[] encryptWithRandomIvWithArraysCopy(byte[] key, byte[] plaintext) throws Exception {
        byte[] randomBytes = new byte[16];
        SecureRandom.getInstanceStrong().nextBytes(randomBytes);

        byte[] iv = Arrays.copyOf(randomBytes, 16);

        GCMParameterSpec ivSpec = new GCMParameterSpec(128, iv);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        cipher.update(plaintext);
        return cipher.doFinal();
    }
}
