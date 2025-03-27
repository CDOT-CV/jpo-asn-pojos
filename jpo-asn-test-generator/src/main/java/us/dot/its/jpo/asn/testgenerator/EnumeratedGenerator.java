package us.dot.its.jpo.asn.testgenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;
import java.util.Random;
import us.dot.its.jpo.asn.runtime.types.Asn1Enumerated;

public class EnumeratedGenerator extends RandomGenerator<Asn1Enumerated> {

  public EnumeratedGenerator(GeneratorOptions options) {
    super(options);
  }

  @Override
  protected Asn1Enumerated construct() {
    try {
      Class<?> clazz = Class.forName(pdu);
      Object[] constants = clazz.getEnumConstants();
      final int numItems = constants.length;
      Random r = new Random();
      int i = r.nextInt(numItems);
      return (Asn1Enumerated)constants[i];
    } catch (ClassNotFoundException  e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void populateRandom(Asn1Enumerated instance) {
    // do nothing here.  Construct create random enum
  }
}
