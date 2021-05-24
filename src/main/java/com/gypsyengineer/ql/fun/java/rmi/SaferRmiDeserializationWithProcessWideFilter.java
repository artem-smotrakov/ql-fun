package com.gypsyengineer.ql.fun.java.rmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.security.Security;

public class SaferRmiDeserializationWithProcessWideFilter {

    public static void main(String[] args)
            throws RemoteException, MalformedURLException, AlreadyBoundException {

        DeserializationConfig.setDeserializationFilterIfNecessary();

        System.out.print("[+] Starting a local registry ... ");
        LocateRegistry.createRegistry(1099);
        System.out.println("okay");

        System.out.print("[+] Bind an object ... ");
        Naming.bind("echo", new Echo());
        System.out.println("okay");
    }
}
