package com.gypsyengineer.ql.fun.java.rmi;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class EchoClient {

    public static void main(String... args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
        EchoInterface echo = (EchoInterface) registry.lookup("echo");
        System.out.println("echo.echoInt(42) = " + echo.echoInt(42));
        System.out.println("echo.echoInt(\"xyz\") = " + echo.echoString("xyz"));
        Object o = new Serializable() {
            @Override
            public String toString() {
                return "serialized object";
            }
        };
        System.out.println("echo.echoObject(o) = " + echo.echoObject(o));
    }
}
