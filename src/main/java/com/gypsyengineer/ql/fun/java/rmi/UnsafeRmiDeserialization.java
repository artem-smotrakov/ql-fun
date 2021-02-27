package com.gypsyengineer.ql.fun.java.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/*
 * Based on https://itnext.io/java-rmi-for-pentesters-part-two-reconnaissance-attack-against-non-jmx-registries-187a6561314d
 */
public class UnsafeRmiDeserialization {

    /*
     * Before starting the server, start a local RMI Registry:
     *
     * CLASSPATH=target/classes rmiregistry 11098
     */
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        System.out.println("[+] Trying to bind");
        Naming.rebind("rmi://127.0.0.1:11098/RMIInterface", new Server());
        System.out.println("[+] Server started");
    }
}
