package us.dot.its.jpo.asn.testgenerator;

import java.util.Random;
import us.dot.its.jpo.asn.runtime.types.Asn1OctetString;

public class OctetStringGenerator extends RandomGenerator<Asn1OctetString> {

  public OctetStringGenerator(GeneratorOptions options) {
    super(options);
  }

  @Override
  protected void populateRandom(Asn1OctetString instance) {
    final int lower = instance.getMinLength();
    final int upper = Math.min(instance.getMaxLength(), 255); // Prevent excessively large if unbounded
    Random r = new Random();
    int len;
    if (lower == upper) {
      len = lower;
    } else {
      len = r.nextInt(upper - lower) + lower;
    }
    byte[] bytes = new byte[len];
    for (int i = 0; i < len; i++) {
      bytes[i] = (byte)r.nextInt(256);
    }
    instance.setOctets(bytes);
  }
}
