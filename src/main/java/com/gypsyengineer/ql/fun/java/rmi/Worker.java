package com.gypsyengineer.ql.fun.java.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Worker extends Remote {

    Object process(Object object) throws RemoteException;
}
