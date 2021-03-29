package com.gypsyengineer.ql.fun.java.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

interface EchoInterface extends Remote {

    Object echoObject(Object obj) throws RemoteException;

    String echoString(String str) throws RemoteException;

    int echoInt(Integer i) throws RemoteException;
}