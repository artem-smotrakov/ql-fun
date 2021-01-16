package com.gypsyengineer.ql.fun.apache.jexl;

import org.apache.commons.jexl2.*;

import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class UnsafeJexl2 {

    public static void main(String... args) throws Exception {
        //runJexlExpression("''.getClass().forName('java.lang.Runtime').getRuntime().exec('gedit')");
        //runJexlExpressionViaGetProperty("class.forName('java.lang.Runtime').getRuntime().exec('gedit')");
        //runJexlExpressionViaJxltEngineExpressionPrepare("${''.getClass().forName('java.lang.Runtime').getRuntime().exec('gedit')}");
    }

    private static void runJexlExpression(String jexlExpr) {
        JexlEngine jexl = new JexlEngine();
        Expression e = jexl.createExpression(jexlExpr);
        JexlContext jc = new MapContext();
        e.evaluate(jc);
    }

    private static void runJexlExpressionWithJexlInfo(String jexlExpr) {
        JexlEngine jexl = new JexlEngine();
        Expression e = jexl.createExpression(
                jexlExpr, new DebugInfo("unknown", 0, 0));
        JexlContext jc = new MapContext();
        e.evaluate(jc);
    }

    private static void runJexlScript(String jexlExpr) {
        JexlEngine jexl = new JexlEngine();
        Script script = jexl.createScript(jexlExpr);
        JexlContext jc = new MapContext();
        script.execute(jc);
    }

    private static void runJexlScriptViaCallable(String jexlExpr) {
        JexlEngine jexl = new JexlEngine();
        Script script = jexl.createScript(jexlExpr);
        JexlContext jc = new MapContext();

        try {
            script.callable(jc).call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void runJexlExpressionViaGetProperty(String jexlExpr) {
        JexlEngine jexl = new JexlEngine();
        jexl.getProperty(new Object(), jexlExpr);
    }

    private static void runJexlExpressionViaSetProperty(String jexlExpr) {
        JexlEngine jexl = new JexlEngine();
        jexl.setProperty(new Object(), jexlExpr, new Object());
    }

    private static void runJexlExpressionViaUnifiedJEXLParseAndEvaluate(String jexlExpr) {
        JexlEngine jexl = new JexlEngine();
        UnifiedJEXL unifiedJEXL = new UnifiedJEXL(jexl);
        unifiedJEXL.parse(jexlExpr).evaluate(new MapContext());
    }

    private static void runJexlExpressionViaUnifiedJEXLParseAndPrepare(String jexlExpr) {
        JexlEngine jexl = new JexlEngine();
        UnifiedJEXL unifiedJEXL = new UnifiedJEXL(jexl);
        unifiedJEXL.parse(jexlExpr).prepare(new MapContext());
    }

    private static void runJexlExpressionViaUnifiedJEXLTemplateEvaluate(String jexlExpr) {
        JexlEngine jexl = new JexlEngine();
        UnifiedJEXL unifiedJEXL = new UnifiedJEXL(jexl);
        unifiedJEXL.createTemplate(jexlExpr).evaluate(new MapContext(), new StringWriter());
    }

    private static void simpleServer(Consumer<String> action) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            try (Socket socket = serverSocket.accept()) {
                byte[] bytes = new byte[1024];
                int n = socket.getInputStream().read(bytes);
                String jexlExpr = new String(bytes, 0, n);
                action.accept(jexlExpr);
            }
        }
    }

    // below are examples of unsafe Jexl usage

    public static void unsafeJexlExpressionEvaluate() throws Exception {
        simpleServer(UnsafeJexl2::runJexlExpression);
    }

    public static void unsafeJexlExpressionEvaluateWithInfo() throws Exception {
        simpleServer(UnsafeJexl2::runJexlExpressionWithJexlInfo);
    }

    public static void unsafeJexlScriptExecute() throws Exception {
        simpleServer(UnsafeJexl2::runJexlScript);
    }

    public static void unsafeJexlScriptCallable() throws Exception {
        simpleServer(UnsafeJexl2::runJexlScriptViaCallable);
    }

    public static void unsafeJexlEngineGetProperty() throws Exception {
        simpleServer(UnsafeJexl2::runJexlExpressionViaGetProperty);
    }

    public static void unsafeJexlEngineSetProperty() throws Exception {
        simpleServer(UnsafeJexl2::runJexlExpressionViaSetProperty);
    }

    public static void unsafeUnifiedJEXLParseAndEvaluate() throws Exception {
        simpleServer(UnsafeJexl2::runJexlExpressionViaUnifiedJEXLParseAndEvaluate);
    }

    public static void unsafeUnifiedJEXLParseAndPrepare() throws Exception {
        simpleServer(UnsafeJexl2::runJexlExpressionViaUnifiedJEXLParseAndPrepare);
    }

    public static void unsafeUnifiedJEXLTemplateEvaluate() throws Exception {
        simpleServer(UnsafeJexl2::runJexlExpressionViaUnifiedJEXLTemplateEvaluate);
    }
}
