package com.gypsyengineer.ql.fun.virtual;

import lombok.RequiredArgsConstructor;

interface Accessor {
  String getValue(String name);
  void setValue(String name, String value);
}

class PublicAccessor implements Accessor {

  public String getValue(String name) {
    return "test";
  }

  public void setValue(String name, String value) {

  }
}

class Container {

  Accessor getAccessor() {
    return new PrivateAccessor("test");
  }

  @RequiredArgsConstructor
  private static class PrivateAccessor implements Accessor {

    private final String filed;

    public String getValue(String name) {
      return "private";
    }

    public void setValue(String name, String value) {

    }
  }

}

public class InnerStaticClassVirtualDispatch {

  public void run() {
    Container container = new Container();
    Accessor accessor = container.getAccessor();
    accessor.setValue("a", "b");

    accessor = new PublicAccessor();
    accessor.setValue("c", "d");
  }
}
