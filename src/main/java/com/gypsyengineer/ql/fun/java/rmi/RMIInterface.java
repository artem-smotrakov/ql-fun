package com.gypsyengineer.ql.fun.java.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

interface RMIInterface extends Remote {

    String echoObject(Object obj) throws RemoteException;

    String echoString(String str) throws RemoteException;

    String echoClass(Class cls) throws RemoteException;

    String echoInt(Integer i) throws RemoteException;
}