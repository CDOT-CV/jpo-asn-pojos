package us.dot.its.jpo.asn.testgenerator;

import java.util.Random;
import us.dot.its.jpo.asn.runtime.types.Asn1Bitstring;

public class BitstringGenerator extends RandomGenerator<Asn1Bitstring> {

  public BitstringGenerator(String pdu, int limit, boolean regional) {
    super(pdu, limit, regional);
  }

  @Override
  protected void populateRandom(Asn1Bitstring instance) {
    Random r = new Random();
    for (int i = 0; i < instance.size(); i++) {
      instance.set(i, r.nextBoolean());
    }
  }


}
