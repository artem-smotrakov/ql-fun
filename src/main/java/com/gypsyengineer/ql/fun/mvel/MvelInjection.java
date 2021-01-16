package com.gypsyengineer.ql.fun.mvel;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import javax.script.CompiledScript;
import javax.script.SimpleScriptContext;
import org.mvel2.MVEL;
import org.mvel2.MVELInterpretedRuntime;
import org.mvel2.MVELRuntime;
import org.mvel2.ParserContext;
import org.mvel2.compiler.CompiledAccExpression;
import org.mvel2.compiler.CompiledExpression;
import org.mvel2.compiler.ExecutableStatement;
import org.mvel2.compiler.ExpressionCompiler;
import org.mvel2.integration.impl.ImmutableDefaultFactory;
import org.mvel2.jsr223.MvelCompiledScript;
import org.mvel2.jsr223.MvelScriptEngine;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

public class MvelInjection {

  public static void testWithMvelEval(Socket socket) throws IOException {
    InputStream in = socket.getInputStream();

    byte[] bytes = new byte[1024];
    int n = in.read(bytes);
    String input = new String(bytes, 0, n); // input is tainted

    MVEL.eval(input);
  }

  public static void testWithMvelCompileAndExecute(Socket socket) throws IOException {
    InputStream in = socket.getInputStream();

    byte[] bytes = new byte[1024];
    int n = in.read(bytes);
    String input = new String(bytes, 0, n); // input is tainted

    Serializable expression = MVEL.compileExpression(input);
    MVEL.executeExpression(expression);
  }

  public static void testWithExpressionCompiler(Socket socket) throws IOException {
    InputStream in = socket.getInputStream();

    byte[] bytes = new byte[1024];
    int n = in.read(bytes);
    String input = new String(bytes, 0, n); // input is tainted

    ExpressionCompiler compiler = new ExpressionCompiler(input);
    ExecutableStatement statement = compiler.compile();
    statement.getValue(new Object(), new ImmutableDefaultFactory());
    statement.getValue(new Object(), new Object(), new ImmutableDefaultFactory());
  }

  public static void testWithCompiledExpressionGetDirectValue(Socket socket) throws IOException {
    InputStream in = socket.getInputStream();

    byte[] bytes = new byte[1024];
    int n = in.read(bytes);
    String input = new String(bytes, 0, n);

    ExpressionCompiler compiler = new ExpressionCompiler(input);
    CompiledExpression statement = compiler.compile();
    statement.getDirectValue(new Object(), new ImmutableDefaultFactory());
  }

  public static void testCompiledAccExpressionGetValue(Socket socket) throws IOException {
    InputStream in = socket.getInputStream();

    byte[] bytes = new byte[1024];
    int n = in.read(bytes);
    String input = new String(bytes, 0, n);

    CompiledAccExpression expression = new CompiledAccExpression(input.toCharArray(), Object.class, new ParserContext());
    expression.getValue(new Object(), new ImmutableDefaultFactory());
  }

  public static void testMvelScriptEngineCompileAndEvaluate(Socket socket) throws Exception {
    InputStream in = socket.getInputStream();

    byte[] bytes = new byte[1024];
    int n = in.read(bytes);
    String input = new String(bytes, 0, n);

    MvelScriptEngine engine = new MvelScriptEngine();
    CompiledScript compiledScript = engine.compile(input);
    compiledScript.eval();

    Serializable script = engine.compiledScript(input);
    engine.evaluate(script, new SimpleScriptContext());
  }

  public static void testMvelCompiledScriptCompileAndEvaluate(Socket socket) throws Exception {
    InputStream in = socket.getInputStream();

    byte[] bytes = new byte[1024];
    int n = in.read(bytes);
    String input = new String(bytes, 0, n);

    MvelScriptEngine engine = new MvelScriptEngine();
    ExpressionCompiler compiler = new ExpressionCompiler(input);
    ExecutableStatement statement = compiler.compile();
    MvelCompiledScript script = new MvelCompiledScript(engine, statement);
    script.eval(new SimpleScriptContext());
  }

  public static void testTemplateRuntimeEval(Socket socket) throws Exception {
    InputStream in = socket.getInputStream();

    byte[] bytes = new byte[1024];
    int n = in.read(bytes);
    String input = new String(bytes, 0, n);

    TemplateRuntime.eval(input, new HashMap());
  }

  public static void testTemplateRuntimeCompileTemplateAndExecute(Socket socket) throws Exception {
    InputStream in = socket.getInputStream();

    byte[] bytes = new byte[1024];
    int n = in.read(bytes);
    String input = new String(bytes, 0, n);

    TemplateRuntime.execute(TemplateCompiler.compileTemplate(input), new HashMap());
  }

  public static void testTemplateRuntimeCompileAndExecute(Socket socket) throws Exception {
    InputStream in = socket.getInputStream();

    byte[] bytes = new byte[1024];
    int n = in.read(bytes);
    String input = new String(bytes, 0, n);

    TemplateCompiler compiler = new TemplateCompiler(input);
    String output = (String) TemplateRuntime.execute(compiler.compile(), new HashMap());
  }

  public static void testMvelRuntimeExecute(Socket socket) throws Exception {
    InputStream in = socket.getInputStream();

    byte[] bytes = new byte[1024];
    int n = in.read(bytes);
    String input = new String(bytes, 0, n);

    ExpressionCompiler compiler = new ExpressionCompiler(input);
    CompiledExpression expression = compiler.compile();
    MVELRuntime.execute(false, expression, new Object(), new ImmutableDefaultFactory());
  }

  public static void main(String... string) throws Exception {
    String template = "java.lang.Runtime.getRuntime().exec(\"gedit\")";
    new MVELInterpretedRuntime(template, new Object(), new ImmutableDefaultFactory());
  }

  public static void mvelEval() {
    MVEL.eval("java.lang.Runtime.getRuntime().exec(\"gedit\")");
  }

}
