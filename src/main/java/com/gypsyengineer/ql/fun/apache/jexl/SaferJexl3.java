package com.gypsyengineer.ql.fun.apache.jexl;

import java.net.ServerSocket;
import java.net.Socket;
import java.security.AccessControlException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.jexl3.*;
import org.apache.commons.jexl3.introspection.*;

public class SaferJexl3 {

    public static void main(String... args) throws Exception {
        runJexlExpressionWithSandbox("''.getClass().forName('java.lang.Runtime').getRuntime().exec('gedit')");
    }

    private static void runJexlExpressionWithSandbox(String jexlExpr) {
        JexlSandbox sandbox = new JexlSandbox(false);
        sandbox.white(SaferJexl3.class.getCanonicalName());
        JexlEngine jexl = new JexlBuilder().sandbox(sandbox).create();
        JexlExpression e = jexl.createExpression(jexlExpr);
        JexlContext jc = new MapContext();
        e.evaluate(jc);
    }

    private static void runJexlExpressionWithUberspectSandbox(String jexlExpr) {
        JexlUberspect sandbox = new JexlUberspectSandbox();
        JexlEngine jexl = new JexlBuilder().uberspect(sandbox).create();
        JexlExpression e = jexl.createExpression(jexlExpr);
        JexlContext jc = new MapContext();
        e.evaluate(jc);
    }

    private static JexlBuilder STATIC_JEXL_BUILDER;

    static {
        JexlSandbox sandbox = new JexlSandbox(false);
        sandbox.white(SaferJexl3.class.getCanonicalName());
        STATIC_JEXL_BUILDER = new JexlBuilder().sandbox(sandbox);
    }

    private static void runJexlExpressionViaJxltEngineWithSandbox(String jexlExpr) {
        JexlEngine jexl = STATIC_JEXL_BUILDER.create();
        JxltEngine jxlt = jexl.createJxltEngine();
        jxlt.createExpression(jexlExpr).evaluate(new MapContext());
    }

    private static class JexlUberspectSandbox implements JexlUberspect {

        private static final List<String> ALLOWED_CLASSES =
                Arrays.asList("java.lang.Math", "java.util.Random");

        private final JexlUberspect uberspect = new JexlBuilder().create().getUberspect();

        @Override
        public JexlMethod getMethod(Object obj, String method, Object... args) {
            if (!ALLOWED_CLASSES.contains(obj.getClass().getCanonicalName())) {
                throw new AccessControlException("Not allowed");
            }
            return uberspect.getMethod(obj, method, args);
        }

        @Override
        public List<PropertyResolver> getResolvers(JexlOperator op, Object obj) {
            return uberspect.getResolvers(op, obj);
        }

        @Override
        public void setClassLoader(ClassLoader loader) {
            uberspect.setClassLoader(loader);
        }

        @Override
        public int getVersion() {
            return uberspect.getVersion();
        }

        @Override
        public JexlMethod getConstructor(Object ctorHandle, Object... args) {
            return uberspect.getConstructor(ctorHandle, args);
        }

        @Override
        public JexlPropertyGet getPropertyGet(Object obj, Object identifier) {
            return uberspect.getPropertyGet(obj, identifier);
        }

        @Override
        public JexlPropertyGet getPropertyGet(List<PropertyResolver> resolvers, Object obj, Object identifier) {
            return uberspect.getPropertyGet(resolvers, obj, identifier);
        }

        @Override
        public JexlPropertySet getPropertySet(Object obj, Object identifier, Object arg) {
            return uberspect.getPropertySet(obj, identifier, arg);
        }

        @Override
        public JexlPropertySet getPropertySet(List<PropertyResolver> resolvers, Object obj, Object identifier, Object arg) {
            return uberspect.getPropertySet(resolvers, obj, identifier, arg);
        }

        @Override
        public Iterator<?> getIterator(Object obj) {
            return uberspect.getIterator(obj);
        }

        @Override
        public JexlArithmetic.Uberspect getArithmetic(JexlArithmetic arithmetic) {
            return uberspect.getArithmetic(arithmetic);
        }
    }

    private static void withSocket(Consumer<String> action) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            try (Socket socket = serverSocket.accept()) {
                byte[] bytes = new byte[1024];
                int n = socket.getInputStream().read(bytes);
                String jexlExpr = new String(bytes, 0, n);
                action.accept(jexlExpr);
            }
        }
    }

    // below are examples of safer Jexl usage

    // with JexlSandbox
    public static void saferJexlExpressionInSandbox() throws Exception {
        withSocket(SaferJexl3::runJexlExpressionWithSandbox);
    }

    // with a custom sandbox implemented with JexlUberspect
    public static void saferJexlExpressionInUberspectSandbox() throws Exception {
        withSocket(SaferJexl3::runJexlExpressionWithUberspectSandbox);
    }

    // with JexlSandbox and JxltEngine
    public static void saferJxltExpressionInSandbox() throws Exception {
        withSocket(SaferJexl3::runJexlExpressionViaJxltEngineWithSandbox);
    }

}
