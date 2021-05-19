package com.gypsyengineer.ql.fun.java.rmi;

import java.io.ObjectInputFilter;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class SaferRmiDeserializationWithObjectInputFilter {

    private static final String[] ALLOWED_PREFIXES = {
            "com.gypsyengineer.ql.fun.java.rmi",
            "java.lang.Boolean", "java.lang.Byte", "java.lang.Character", "java.lang.Double", "java.lang.Enum",
            "java.lang.Float", "java.lang.Integer", "java.lang.Long", "java.lang.Number", "java.lang.Object",
            "java.lang.Short",
            "java.util",
            "java.rmi"
    };

    public static void main(String[] args) throws Exception {
        System.out.print("[+] Starting a local registry ... ");
        LocateRegistry.createRegistry(1099);
        System.out.println("okay");

        System.out.print("[+] Bind an object ... ");
        ObjectInputFilter filter = info -> {
            ObjectInputFilter serialFilter = ObjectInputFilter.Config.getSerialFilter();
            if (serialFilter != null) {
                ObjectInputFilter.Status status = serialFilter.checkInput(info);
                if (status != ObjectInputFilter.Status.UNDECIDED) {
                    return status;
                }
            }

            Class<?> clazz = info.serialClass();
            if (clazz == null) {
                return ObjectInputFilter.Status.UNDECIDED;
            }

            for (String prefix : ALLOWED_PREFIXES) {
                if (clazz.getCanonicalName() != null && clazz.getCanonicalName().startsWith(prefix)) {
                    return ObjectInputFilter.Status.ALLOWED;
                }
                if (clazz.getPackageName().startsWith(prefix)) {
                    return ObjectInputFilter.Status.ALLOWED;
                }
            }

            System.err.println("DEBUG: " + clazz.getCanonicalName());
            return ObjectInputFilter.Status.REJECTED;
        };
        Naming.bind("echo", UnicastRemoteObject.exportObject(new Echo(), 12345, filter));
        System.out.println("okay");
    }
}
