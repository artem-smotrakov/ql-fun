package com.gypsyengineer.ql.fun.java.rmi;

import java.rmi.RemoteException;

// thia class doesn't extend UnicastRemoteObject
public class WorkerImpl implements Worker {

    @Override
    public Object process(Object object) throws RemoteException {
        return object;
    }
}
