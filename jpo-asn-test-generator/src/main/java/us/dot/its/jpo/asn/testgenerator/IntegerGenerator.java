package us.dot.its.jpo.asn.testgenerator;

import java.util.Random;
import us.dot.its.jpo.asn.runtime.types.Asn1Integer;

public class IntegerGenerator extends RandomGenerator<Asn1Integer> {

  public IntegerGenerator(GeneratorOptions options) {
    super(options);
  }

  @Override
  public void populateRandom(Asn1Integer instance) {
    Random r = new Random();
    long lowerBound = instance.getLowerBound();
    long upperBound = instance.getUpperBound();
    long rand = r.nextLong(upperBound - lowerBound) + lowerBound;
    instance.setValue(rand);
  }
}
