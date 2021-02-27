package com.gypsyengineer.ql.fun.java.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements RMIInterface {

    @Override
    public String echoObject(Object obj) throws RemoteException {
        return "Object: " + obj.toString();
    }

    @Override
    public String echoString(String str) throws RemoteException {
        return "String: " + str.toString();
    }

    @Override
    public String echoClass(Class cls) throws RemoteException {
        return "Class: " + cls.toString();
    }

    @Override
    public String echoInt(Integer i) throws RemoteException {
        Integer X = i;
        return "Integer";
    }

    protected Server() throws RemoteException {
        super();
    }

}
