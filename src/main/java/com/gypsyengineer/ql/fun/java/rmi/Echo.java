package com.gypsyengineer.ql.fun.java.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

interface Echo extends Remote {

    Object echoObject(Object obj) throws RemoteException;

    String echoString(String str) throws RemoteException;

    Integer echoInteger(Integer i) throws RemoteException;
}