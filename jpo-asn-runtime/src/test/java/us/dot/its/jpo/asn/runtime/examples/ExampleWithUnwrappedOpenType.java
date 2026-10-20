package us.dot.its.jpo.asn.runtime.examples;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import us.dot.its.jpo.asn.runtime.serialization.OpenTypeDeserializer;
import us.dot.its.jpo.asn.runtime.serialization.OpenTypeSerializer;
import us.dot.its.jpo.asn.runtime.serialization.RootSequenceOfSerializer;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;
import us.dot.its.jpo.asn.runtime.types.Asn1SequenceOf;

/**
 * Open type example whose value is a SEQUENCE OF, so the serializer has no wrapper name, like the
 * generated J2735 ProbeDataConfigMessageMessageFrame.  The wrapper element is written by Jackson
 * because the value is a collection.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("MessageFrame")
@JsonPropertyOrder({"messageId", "value"})
public class ExampleWithUnwrappedOpenType extends Asn1Sequence {

  @Getter
  @Setter
  private AInteger messageId;

  private SequenceOfASequence value;

  @JsonSerialize(using = ValueSerializer.class)
  public SequenceOfASequence getValue() {
    return value;
  }

  @JsonDeserialize(using = ValueDeserializer.class)
  public void setValue(SequenceOfASequence value) {
    this.value = value;
  }

  @JsonSerialize(using = RootSequenceOfSerializer.class)
  public static class SequenceOfASequence extends Asn1SequenceOf<ASequence> {
    public SequenceOfASequence() {
      super(ASequence.class, 1L, 10L);
    }
  }

  public static class ValueSerializer extends OpenTypeSerializer<SequenceOfASequence> {
    protected ValueSerializer() {
      super(SequenceOfASequence.class, null, "SequenceOfASequence");
    }
  }

  public static class ValueDeserializer extends OpenTypeDeserializer<SequenceOfASequence> {
    protected ValueDeserializer() {
      super(SequenceOfASequence.class, "SequenceOfASequence");
    }
  }

}
