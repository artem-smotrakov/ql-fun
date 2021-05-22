package com.gypsyengineer.ql.fun.java.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class EchoImpl extends UnicastRemoteObject implements Echo {

    protected EchoImpl() throws RemoteException {
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
    public Integer echoInteger(Integer i) throws RemoteException {
        return i;
    }

    // this doesn't belong to EchoInterface
    public Object otherMethod(Object obj) {
        return new Object();
    }

}
