package com.gypsyengineer.ql.fun.apache.jexl;

import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

import org.apache.commons.jexl3.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class RunProcessWithJexl3 {

    public static void main(String... args) throws Exception {
        runJexlExpressionViaCallable("''.getClass().forName('java.lang.Runtime').getRuntime().exec('gedit')");
        //runJexlExpressionViaGetProperty("class.forName('java.lang.Runtime').getRuntime().exec('gedit')");
        //runJexlExpressionViaJxltEngineExpressionPrepare("${''.getClass().forName('java.lang.Runtime').getRuntime().exec('gedit')}");
    }

    private static void runJexlExpression(String jexlExpr) {
        JexlEngine jexl = new JexlBuilder().create();
        JexlExpression e = jexl.createExpression(jexlExpr);
        JexlContext jc = new MapContext();
        e.evaluate(jc);
    }

    private static void runJexlExpressionViaCallable(String jexlExpr) {
        JexlEngine jexl = new JexlBuilder().create();
        JexlExpression e = jexl.createExpression(jexlExpr);
        JexlContext jc = new MapContext();

        try {
            e.callable(jc).call();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void runJexlExpressionWithJexlInfo(String jexlExpr) {
        JexlEngine jexl = new JexlBuilder().create();
        JexlExpression e = jexl.createExpression(new JexlInfo("unknown", 0, 0), jexlExpr);
        JexlContext jc = new MapContext();
        e.evaluate(jc);
    }

    private static void runJexlScript(String jexlExpr) {
        JexlEngine jexl = new JexlBuilder().create();
        JexlScript script = jexl.createScript(jexlExpr);
        JexlContext jc = new MapContext();
        script.execute(jc);
    }

    private static void runJexlScriptViaCallable(String jexlExpr) {
        JexlEngine jexl = new JexlBuilder().create();
        JexlScript script = jexl.createScript(jexlExpr);
        JexlContext jc = new MapContext();

        try {
            script.callable(jc).call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void runJexlExpressionViaGetProperty(String jexlExpr) {
        JexlEngine jexl = new JexlBuilder().create();
        jexl.getProperty(new Object(), jexlExpr);
    }

    private static void runJexlExpressionViaSetProperty(String jexlExpr) {
        JexlEngine jexl = new JexlBuilder().create();
        jexl.setProperty(new Object(), jexlExpr, new Object());
    }

    private static void runJexlExpressionViaJxltEngineExpressionEvaluate(String jexlExpr) {
        JexlEngine jexl = new JexlBuilder().create();
        JxltEngine jxlt = jexl.createJxltEngine();
        jxlt.createExpression(jexlExpr).evaluate(new MapContext());
    }

    private static void runJexlExpressionViaJxltEngineExpressionPrepare(String jexlExpr) {
        JexlEngine jexl = new JexlBuilder().create();
        JxltEngine jxlt = jexl.createJxltEngine();
        jxlt.createExpression(jexlExpr).prepare(new MapContext());
    }

    private static void runJexlExpressionViaJxltEngineTemplateEvaluate(String jexlExpr) {
        JexlEngine jexl = new JexlBuilder().create();
        JxltEngine jxlt = jexl.createJxltEngine();
        jxlt.createTemplate(jexlExpr).evaluate(new MapContext(), new StringWriter());
    }

    private static void testWithSocket(Consumer<String> action) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            try (Socket socket = serverSocket.accept()) {
                byte[] bytes = new byte[1024];
                int n = socket.getInputStream().read(bytes);
                String jexlExpr = new String(bytes, 0, n);
                action.accept(jexlExpr);
            }
        }
    }

    // below are tests for the query

    public static void testWithJexlExpressionEvaluate() throws Exception {
        testWithSocket(RunProcessWithJexl3::runJexlExpression);
    }

    public static void testWithJexlExpressionEvaluateWithInfo() throws Exception {
        testWithSocket(RunProcessWithJexl3::runJexlExpressionWithJexlInfo);
    }

    public static void testWithJexlScriptExecute() throws Exception {
        testWithSocket(RunProcessWithJexl3::runJexlScript);
    }

    public static void testWithJexlScriptCallable() throws Exception {
        testWithSocket(RunProcessWithJexl3::runJexlScriptViaCallable);
    }

    public static void testWithJexlEngineGetProperty() throws Exception {
        testWithSocket(RunProcessWithJexl3::runJexlExpressionViaGetProperty);
    }

    public static void testWithJexlEngineSetProperty() throws Exception {
        testWithSocket(RunProcessWithJexl3::runJexlExpressionViaSetProperty);
    }

    public static void testWithJxltEngineExpressionEvaluate() throws Exception {
        testWithSocket(RunProcessWithJexl3::runJexlExpressionViaJxltEngineExpressionEvaluate);
    }

    public static void testWithJxltEngineExpressionPrepare() throws Exception {
        testWithSocket(RunProcessWithJexl3::runJexlExpressionViaJxltEngineExpressionPrepare);
    }

    public static void testWithJxltEngineTemplateEvaluate() throws Exception {
        testWithSocket(RunProcessWithJexl3::runJexlExpressionViaJxltEngineTemplateEvaluate);
    }

    public static void testWithJexlExpressionCallable() throws Exception {
        testWithSocket(RunProcessWithJexl3::runJexlExpressionViaCallable);
    }

    @PostMapping("/request")
    public ResponseEntity testSpringControllerThatEvaluatesJexlFromPathVariable(
            @PathVariable String expr) {

        runJexlExpression(expr);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/request")
    public ResponseEntity testSpringControllerThatEvaluatesJexlFromRequestBody(
            @RequestBody Data data) {

        String expr = data.getExpr();
        runJexlExpression(expr);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/request")
    public ResponseEntity testSpringControllerThatEvaluatesJexlFromRequestBodyWithNestedObjects(
            @RequestBody CustomRequest customRequest) {

        String expr = customRequest.getData().getExpr();
        runJexlExpression(expr);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    public static class CustomRequest {

        private Data data;

        CustomRequest(Data data) {
            this.data = data;
        }

        public Data getData() {
            return data;
        }
    }

    public static class Data {

        private String expr;

        Data(String expr) {
            this.expr = expr;
        }

        public String getExpr() {
            return expr;
        }
    }
}
