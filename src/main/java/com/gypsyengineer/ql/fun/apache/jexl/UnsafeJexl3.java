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

public class UnsafeJexl3 {

    public static void main(String... args) throws Exception {
        //runJexlExpressionViaCallable("''.getClass().forName('java.lang.Runtime').getRuntime().exec('gedit')");
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

    // below are examples of unsafe Jexl usage

    public static void unsafeJexlExpressionEvaluate() throws Exception {
        withSocket(UnsafeJexl3::runJexlExpression);
    }

    public static void unsafeJexlExpressionEvaluateWithInfo() throws Exception {
        withSocket(UnsafeJexl3::runJexlExpressionWithJexlInfo);
    }

    public static void unsafeJexlScriptExecute() throws Exception {
        withSocket(UnsafeJexl3::runJexlScript);
    }

    public static void unsafeJexlScriptCallable() throws Exception {
        withSocket(UnsafeJexl3::runJexlScriptViaCallable);
    }

    public static void unsafeJexlEngineGetProperty() throws Exception {
        withSocket(UnsafeJexl3::runJexlExpressionViaGetProperty);
    }

    public static void unsafeJexlEngineSetProperty() throws Exception {
        withSocket(UnsafeJexl3::runJexlExpressionViaSetProperty);
    }

    public static void unsafeJxltEngineExpressionEvaluate() throws Exception {
        withSocket(UnsafeJexl3::runJexlExpressionViaJxltEngineExpressionEvaluate);
    }

    public static void unsafeJxltEngineExpressionPrepare() throws Exception {
        withSocket(UnsafeJexl3::runJexlExpressionViaJxltEngineExpressionPrepare);
    }

    public static void unsafeJxltEngineTemplateEvaluate() throws Exception {
        withSocket(UnsafeJexl3::runJexlExpressionViaJxltEngineTemplateEvaluate);
    }

    public static void unsafeJexlExpressionCallable() throws Exception {
        withSocket(UnsafeJexl3::runJexlExpressionViaCallable);
    }

    @PostMapping("/request")
    public ResponseEntity unsafeSpringControllerThatEvaluatesJexlFromPathVariable(
            @PathVariable String expr) {

        runJexlExpression(expr);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/request")
    public ResponseEntity unsafeSpringControllerThatEvaluatesJexlFromRequestBody(
            @RequestBody Data data) {

        String expr = data.getExpr();
        runJexlExpression(expr);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/request")
    public ResponseEntity unsafeSpringControllerThatEvaluatesJexlFromRequestBodyWithNestedObjects(
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
