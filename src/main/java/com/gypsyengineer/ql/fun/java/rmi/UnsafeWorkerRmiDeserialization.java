package com.gypsyengineer.ql.fun.java.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class UnsafeWorkerRmiDeserialization {

    public static void main(String[] args) throws Exception {
        System.out.print("[+] Starting a local registry ... ");
        LocateRegistry.createRegistry(1099);
        System.out.println("okay");

        System.out.print("[+] Bind an object ... ");
        Naming.bind("worker", UnicastRemoteObject.exportObject(new WorkerImpl(), 23456));
        System.out.println("okay");
    }
}
