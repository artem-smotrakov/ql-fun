package com.gypsyengineer.ql.fun.spel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

public class SaferSpelExpressionEvaluation {

  public Object evaluate(Socket socket) throws IOException {
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(socket.getInputStream()))) {

      String string = reader.readLine();
      ExpressionParser parser = new SpelExpressionParser();
      Expression expression = parser.parseExpression(string);
      SimpleEvaluationContext context
          = SimpleEvaluationContext.forReadWriteDataBinding().build();
      return expression.getValue(context);
    }
  }

}
