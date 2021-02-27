package com.gypsyengineer.ql.fun.juel;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

import javax.el.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class UnsafeJuel {

    // TODO: Is createValueExpression(Object instance, Class<?> expectedType) a step?

    private static final String PAYLOAD = "${''.getClass().forName('java.lang.Runtime').getMethods()[13].invoke(''.getClass().forName('java.lang.Runtime').getMethods()[6].invoke(''.getClass().forName('java.lang.Runtime')), 'gedit')}";

    public static void main(String... args) {
        //runLambdaExpression("${1+2}");
        //createFactoryWithElManagerSetVariable("${1+2}");
        //runJuelMethodExpressionInvoke(PAYLOAD);
        runJuelValueExpressionGetValue(PAYLOAD);
    }
/**
    // sink ELProcessor.eval()
    private static void runExpressionViaELProcessorEval(String expression) {
        ELProcessor processor = new ELProcessor();
        System.out.println(processor.eval(expression));
    }

    // sink ELProcessor.getValue()
    private static void runExpressionViaELProcessorGetValue(String expression) {
        ELProcessor processor = new ELProcessor();
        System.out.println(processor.getValue(expression, Object.class));
    }

    // sink ELProcessor.setValue()
    private static void runExpressionViaELProcessorSetValue(String expression) {
        ELProcessor processor = new ELProcessor();
        processor.setValue(expression, new Object());
    }

    // sink ELProcessor.setVariable() (???)
    private static void runExpressionViaELProcessorSetVariable(String expression) {
        ELProcessor processor = new ELProcessor();
        processor.setVariable("test", expression);
        System.out.println(processor.eval("test"));
    }

    // sink LambdaExpression.invoke()
    private static void runLambdaExpression(String expression) {
        ExpressionFactory factory = ELManager.getExpressionFactory();
        StandardELContext context = new StandardELContext(factory);
        ValueExpression valueExpression = factory.createValueExpression(context, expression, Object.class);
        LambdaExpression lambdaExpression = new LambdaExpression(new ArrayList<>(), valueExpression);
        System.out.println(lambdaExpression.invoke(context, new Object[0]));
    }

    private static void createFactoryWithElManager(String expression) {
        ExpressionFactory factory = ELManager.getExpressionFactory();
        StandardELContext context = new StandardELContext(factory);
        System.out.println(factory.createValueExpression(context, expression, Object.class).getValue(context));
    }

    // sink ELManager.setVariable() (???)
    private static void createFactoryWithElManagerSetVariable(String expression) {
        ExpressionFactory factory = ELManager.getExpressionFactory();
        StandardELContext context = new StandardELContext(factory);
        ELManager manager = new ELManager();
        manager.setELContext(context);
        manager.setVariable("test", factory.createValueExpression(context, expression, Object.class));
        System.out.println(factory.createValueExpression(context, "${test}", Object.class).getValue(context));
    }
*/
    // JUEL

    // only getValue() and setValue() seem to trigger evaluation
    private static void runJuelValueExpressionGetValue(String expression) {
        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();
        ValueExpression e = factory.createValueExpression(context, expression, Object.class);
        System.out.println(e.getValue(context));
    }

    private static void runJuelValueExpressionSetValue(String expression) {
        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();
        ValueExpression e = factory.createValueExpression(context, expression, Object.class);
        e.setValue(context, new Object());
    }

    // for sink MethodExpression.invoke()
    private static void runJuelMethodExpressionInvoke(String expression) {
        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();
        MethodExpression e = factory.createMethodExpression(context, expression, Object.class, new Class[0]);
        System.out.println(e.invoke(context, new Object[0]));
    }

    // MethodExpression.getMethodInfo() is not a sink
    private static void runJuelMethodExpressionMethodInfo(String expression) {
        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();
        MethodExpression e = factory.createMethodExpression(context, expression, Object.class, new Class[0]);
        System.out.println(e.getMethodInfo(context));
    }
/*
    // tests for the query

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

    private static void testWithExpressionViaELProcessorEval() throws Exception {
        withSocket(UnsafeJuel::runExpressionViaELProcessorEval);
    }

    private static void testWithExpressionViaELProcessorGetValue() throws Exception {
        withSocket(UnsafeJuel::runExpressionViaELProcessorGetValue);
    }

    private static void testWithExpressionViaELProcessorSetValue() throws Exception {
        withSocket(UnsafeJuel::runExpressionViaELProcessorSetValue);
    }

    private static void testWithLambdaExpression() throws Exception {
        withSocket(UnsafeJuel::runLambdaExpression);
    }

    private static void testWithCreateFactoryWithElManager() throws Exception {
        withSocket(UnsafeJuel::createFactoryWithElManager);
    }

    private static void testWithJuelValueExpressionGetValue() throws Exception {
        withSocket(UnsafeJuel::runJuelValueExpressionGetValue);
    }

    private static void testJuelValueExpressionSetValue() throws Exception {
        withSocket(UnsafeJuel::runJuelValueExpressionSetValue);
    }

    private static void testWithJuelMethodExpressionInvoke() throws Exception {
        withSocket(UnsafeJuel::runJuelMethodExpressionInvoke);
    }
 */
}
