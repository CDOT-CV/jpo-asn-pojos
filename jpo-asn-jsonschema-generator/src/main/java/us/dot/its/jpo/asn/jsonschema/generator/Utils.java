package us.dot.its.jpo.asn.jsonschema.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Utils {

  @SuppressWarnings({"rawtypes"})
  public static Class getClassFromName(final String fullyQualifiedName) {
    Class clazz;
    try {
      clazz = Class.forName(fullyQualifiedName);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return clazz;
  }

  @SuppressWarnings({"unchecked"})
  public static <T> T construct(Class<T> clazz) {
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
