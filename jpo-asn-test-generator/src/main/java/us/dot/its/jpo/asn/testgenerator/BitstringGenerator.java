package us.dot.its.jpo.asn.testgenerator;

import java.util.Random;
import us.dot.its.jpo.asn.runtime.types.Asn1Bitstring;

public class BitstringGenerator extends RandomGenerator<Asn1Bitstring> {

  public BitstringGenerator(GeneratorOptions options) {
    super(options);
  }

  @Override
  protected void populateRandom(Asn1Bitstring instance) {
    Random r = new Random();
    for (int i = 0; i < instance.size(); i++) {
      instance.set(i, r.nextBoolean());
    }
  }


}
