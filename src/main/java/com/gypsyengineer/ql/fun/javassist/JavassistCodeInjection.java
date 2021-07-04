package com.gypsyengineer.ql.fun.javassist;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.InputStream;
import java.net.Socket;

public class JavassistCodeInjection {

    public static void main(String[] args) throws Exception {
        ClassPool cp = ClassPool.getDefault();
        cp.insertClassPath(new ClassClassPath(JavassistCodeInjection.class));
        CtClass cc = cp.get("com.gypsyengineer.ql.fun.javassist.Hello");
        CtMethod m = cc.getDeclaredMethod("say");
        m.insertBefore("{ System.out.println(\"Hello.say():\"); }");
        Class c = cc.toClass();
        Hello h = (Hello) c.newInstance();
        h.say();
    }

    // below are tests for a CodeQL query

    public void unsafeJavassist(Socket socket) throws Exception {
        try (InputStream is = socket.getInputStream()) {
            ClassPool cp = ClassPool.getDefault();
            cp.insertClassPath(new ClassClassPath(JavassistCodeInjection.class));
            CtClass cc = cp.get("com.gypsyengineer.ql.fun.javassist.Hello");
            CtMethod m = cc.getDeclaredMethod("say");
            byte[] code = new byte[1024];
            int n = is.read(code);
            m.insertBefore(new String(code, 0, n));
            Class c = cc.toClass();
            Hello h = (Hello) c.newInstance();
            h.say();
        }
    }
}

