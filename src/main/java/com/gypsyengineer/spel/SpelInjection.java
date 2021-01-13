package com.gypsyengineer.spel;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpelInjection {

  // SpelExpressionParser is a powerful expression parser
  private static final ExpressionParser PARSER = new SpelExpressionParser();

  public void testBad01(Socket socket) throws IOException {
    InputStream in = socket.getInputStream();

    byte[] bytes = new byte[1024];
    int n = in.read(bytes);
    String input = new String(bytes, 0, n); // input is tainted

    // SpelExpressionParser is a powerful expression parser
    ExpressionParser parser = new SpelExpressionParser();

    // parsing tainted data produces a tainted Expression
    Expression expression = parser.parseExpression(input);

    // evaluates the tainted expression - this should be caught
    expression.getValue();
  }

  public void testBad02(Socket socket) throws IOException {
    InputStream in = socket.getInputStream();

    byte[] bytes = new byte[1024];
    int n = in.read(bytes);
    String input = new String(bytes, 0, n); // input is tainted

    // SpelExpressionParser is a powerful expression parser
    // parsing tainted data produces a tainted Expression
    Expression expression = new SpelExpressionParser().parseExpression(input);

    // evaluates the tainted expression - this should be caught
    expression.getValue();
  }

  public void testBad03(Socket socket) throws IOException {
    InputStream in = socket.getInputStream();

    byte[] bytes = new byte[1024];
    int n = in.read(bytes);
    String input = new String(bytes, 0, n); // input is tainted

    // SpelExpressionParser is a powerful expression parser
    // parsing tainted data produces a tainted Expression
    Expression expression = new SpelExpressionParser().parseExpression(input);

    Object root = new Object();
    Object value = new Object();

    // evaluates the tainted expression - this should be caught
    expression.setValue(root, value);
  }

  public void testBad04(Socket socket) throws IOException {
    InputStream in = socket.getInputStream();

    byte[] bytes = new byte[1024];
    int n = in.read(bytes);
    String input = new String(bytes, 0, n); // input is tainted

    // parsing tainted data produces a tainted Expression
    Expression expression = PARSER.parseExpression(input);

    // evaluates the tainted expression - this should be caught
    expression.getValue();
  }

  public void testBad05(Socket socket) throws IOException {
    InputStream in = socket.getInputStream();

    byte[] bytes = new byte[1024];
    int n = in.read(bytes);
    String input = new String(bytes, 0, n); // input is tainted

    // parsing tainted data produces a tainted Expression
    Expression expression = PARSER.parseExpression(input);

    // figuring out the type of value that can be setValue causes evaluation of the expression
    expression.getValueType();
  }

  public void testBad06(Socket socket) throws IOException {
    InputStream in = socket.getInputStream();

    byte[] bytes = new byte[1024];
    int n = in.read(bytes);
    String input = new String(bytes, 0, n); // input is tainted

    // parsing tainted data produces a tainted Expression
    Expression expression = PARSER.parseExpression(input);

    StandardEvaluationContext context = new StandardEvaluationContext();

    // the expression is evaluated in a powerful context
    expression.getValue(context);
  }

  public void testGood01(Socket socket) throws IOException {
    InputStream in = socket.getInputStream();

    byte[] bytes = new byte[1024];
    int n = in.read(bytes);
    String input = new String(bytes, 0, n); // input is tainted

    // parsing tainted data produces a tainted Expression
    Expression expression = PARSER.parseExpression(input);

    SimpleEvaluationContext context = SimpleEvaluationContext.forReadWriteDataBinding().build();

    // the expression is evaluated in a restricted context
    expression.getValue(context);
  }

}
