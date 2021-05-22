package com.gypsyengineer.ql.fun.java.rmi;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class WorkerClient {

    public static void main(String... args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
        Worker worker = (Worker) registry.lookup("worker");
        Object o = new Serializable() {
            @Override
            public String toString() {
                return "serialized object";
            }
        };
        System.out.println("worker.process(o) = " + worker.process(o));
    }
}
