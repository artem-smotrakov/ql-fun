package com.gypsyengineer.ql.fun.java.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Echo extends UnicastRemoteObject implements EchoInterface {

    protected Echo() throws RemoteException {
        super();
    }

    @Override
    public Object echoObject(Object obj) throws RemoteException {
        return obj;
    }

    @Override
    public String echoString(String str) throws RemoteException {
        return str;
    }

    @Override
    public int echoInt(Integer i) throws RemoteException {
        return i;
    }

}
