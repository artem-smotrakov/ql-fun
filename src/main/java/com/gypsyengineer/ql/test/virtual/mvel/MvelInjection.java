package com.gypsyengineer.ql.test.virtual.mvel;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.Socket;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.mvel2.compiler.CompiledAccExpression;
import org.mvel2.compiler.CompiledExpression;
import org.mvel2.compiler.ExecutableStatement;
import org.mvel2.compiler.ExpressionCompiler;
import org.mvel2.integration.impl.ImmutableDefaultFactory;
import org.mvel2.jsr223.MvelCompiledScript;
import org.mvel2.jsr223.MvelScriptEngine;
import org.mvel2.jsr223.MvelScriptEngineFactory;

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

  public static void main(String... string) throws Exception {
    MvelScriptEngine engine = new MvelScriptEngine();
    ExpressionCompiler compiler = new ExpressionCompiler("java.lang.Runtime.getRuntime().exec(\"gedit\")");
    ExecutableStatement statement = compiler.compile();
    MvelCompiledScript script = new MvelCompiledScript(engine, statement);
    script.eval(new SimpleScriptContext());
  }

  public static void mvelEval() {
    MVEL.eval("java.lang.Runtime.getRuntime().exec(\"gedit\")");
  }

}
