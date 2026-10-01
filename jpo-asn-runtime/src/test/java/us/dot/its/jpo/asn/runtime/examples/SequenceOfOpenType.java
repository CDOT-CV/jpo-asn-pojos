package us.dot.its.jpo.asn.runtime.examples;

import us.dot.its.jpo.asn.runtime.serialization.SequenceOfOpenTypeSerializer;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;
import us.dot.its.jpo.asn.runtime.types.Asn1SequenceOf;

public class SequenceOfOpenType extends Asn1SequenceOf<Asn1Sequence> {

  public SequenceOfOpenType() {
    super(Asn1Sequence.class, 1L, 5L);
  }

  public static class SequenceOfOpenTypeTestSerializer
      extends SequenceOfOpenTypeSerializer<Asn1Sequence, SequenceOfOpenType> {

    public SequenceOfOpenTypeTestSerializer() {
      super(Asn1Sequence.class, SequenceOfOpenType.class);
    }
  }
}
