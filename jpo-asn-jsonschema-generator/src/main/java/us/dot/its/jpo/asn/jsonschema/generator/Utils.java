package us.dot.its.jpo.asn.jsonschema.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import us.dot.its.jpo.asn.runtime.types.IA5String;

public class Utils {

  @SuppressWarnings({"rawtypes"})
  public static Class getClassFromName(final String fullyQualifiedName) {
    // Handle primitive types
    switch (fullyQualifiedName) {
      case "boolean":
        return boolean.class;
      case "byte":
        return byte.class;
      case "char":
        return char.class;
      case "double":
        return double.class;
      case "float":
        return float.class;
      case "int":
        return int.class;
      case "long":
        return long.class;
      case "short":
        return short.class;
      case "void":
        return void.class;
      default:
        try {
          return Class.forName(fullyQualifiedName);
        } catch (ClassNotFoundException e) {
          throw new RuntimeException(e);
        }
    }
  }

  @SuppressWarnings({"unchecked"})
  public static <T> T construct(Class<T> clazz) {
    if (clazz.getName().endsWith("IA5String")) {
      // Raw IA5String doesn't have parameterless constructor
      return (T)new IA5String("");
    }
    try {
      Constructor<?> cons = clazz.getDeclaredConstructor();
      return (T) cons.newInstance();
    } catch (NoSuchMethodException
             | InstantiationException
             | IllegalAccessException
             | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}
