package us.dot.its.jpo.asn.testgenerator;

import java.util.Random;
import us.dot.its.jpo.asn.runtime.types.IA5String;

public class IA5StringGenerator extends RandomGenerator<IA5String> {

  public IA5StringGenerator(GeneratorOptions options) {
    super(options);
  }


  @Override
  protected void populateRandom(IA5String instance) {
    final int lower = instance.getMinLength();
    final int upper = Math.min(instance.getMaxLength(), 255); // Prevent excessively large if unbounded
    Random r = new Random();
    int strLength = (upper == lower) ? lower : r.nextInt(upper - lower) + lower;
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < strLength; i++) {
      final int letter = r.nextInt('Z' - 'A') + 'A';
      sb.append(Character.valueOf((char)letter));
    }
    instance.setValue(sb.toString());
  }
}
