package us.dot.its.jpo.asn.testgenerator;

import java.util.Random;
import us.dot.its.jpo.asn.runtime.types.Asn1SequenceOf;
import us.dot.its.jpo.asn.runtime.types.Asn1Type;

public class SequenceOfGenerator extends RandomGenerator<Asn1SequenceOf<?>> {

  public SequenceOfGenerator(GeneratorOptions options) {
    super(options);
  }

  @SuppressWarnings({"unchecked"})
  @Override
  protected void populateRandom(Asn1SequenceOf<?> instance) {
    final long lower = instance.getSizeLowerBound();
    final long upper = instance.getSizeUpperBound();
    final long effectiveUpper = (sequenceOfLimit < upper && sequenceOfLimit > lower) ? sequenceOfLimit : upper;
    Random r = new Random();
    final long numItems = r.nextLong(effectiveUpper - lower) + lower;
    Class<?> itemClass = instance.getItemClass();
    var itemGen = getGeneratorForType((Class<Asn1Type>)itemClass, options());
    if (itemGen != null) {
      for (int i = 0; i < numItems; i++) {
        instance.add(itemGen.createRandom());
      }
    } else {
      System.err.printf("No generator available for item type %s%n", itemClass.getName());
    }
  }
}
