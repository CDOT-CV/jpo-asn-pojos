package us.dot.its.jpo.asn.testgenerator;

import java.util.Random;
import us.dot.its.jpo.asn.runtime.types.Asn1Boolean;

public class BooleanGenerator extends RandomGenerator<Asn1Boolean>{

  public BooleanGenerator(String pdu, int sequenceOfLimit, boolean regional) {
    super(pdu, sequenceOfLimit, regional);
  }

  @Override
  protected void populateRandom(Asn1Boolean instance) {
    Random r = new Random();
    instance.setValue(r.nextBoolean());
  }
}
