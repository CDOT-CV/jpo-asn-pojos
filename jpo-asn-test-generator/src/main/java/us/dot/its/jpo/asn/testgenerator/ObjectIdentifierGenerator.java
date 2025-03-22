package us.dot.its.jpo.asn.testgenerator;

import java.util.Random;
import us.dot.its.jpo.asn.runtime.types.Asn1ObjectIdentifier;

public class ObjectIdentifierGenerator extends RandomGenerator<Asn1ObjectIdentifier>{

  public ObjectIdentifierGenerator(String pdu, int sequenceOfLimit, boolean regional) {
    super(pdu, sequenceOfLimit, regional);
  }

  @Override
  protected void populateRandom(Asn1ObjectIdentifier instance) {
      Random r = new Random();
      String str = String.format("1.0.%s.1", randomShort(r));
      instance.setValue(str);
  }

  private short randomShort(Random r) {
    return (short)r.nextInt(Short.MAX_VALUE);
  }
}
