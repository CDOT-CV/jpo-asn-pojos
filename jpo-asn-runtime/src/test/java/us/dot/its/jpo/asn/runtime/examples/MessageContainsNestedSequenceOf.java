package us.dot.its.jpo.asn.runtime.examples;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;
import us.dot.its.jpo.asn.runtime.serialization.NestedSequenceOfDeserializer;
import us.dot.its.jpo.asn.runtime.serialization.NestedSequenceOfSerializer;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;
import us.dot.its.jpo.asn.runtime.types.Asn1SequenceOf;

/**
 * SEQUENCE containing anonymous nested SEQUENCE OF INTEGER and SEQUENCE OF SEQUENCE types, modeled
 * after the generated J2735 VehicleAxlesAndWeightInfo and TravelerDataFrame classes.
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"integers", "sequences"})
@Getter
@Setter
public class MessageContainsNestedSequenceOf extends Asn1Sequence {

  @JsonProperty("integers")
  @JsonSerialize(using = SequenceOfIntegersSerializer.class)
  @JsonDeserialize(using = SequenceOfIntegersDeserializer.class)
  private SequenceOfIntegers integers;

  @JsonProperty("sequences")
  @JsonSerialize(using = SequenceOfSequencesSerializer.class)
  @JsonDeserialize(using = SequenceOfSequencesDeserializer.class)
  private SequenceOfSequences sequences;

  @JsonInclude(Include.NON_NULL)
  public static class SequenceOfIntegers extends Asn1SequenceOf<AInteger> {
    public SequenceOfIntegers() {
      super(AInteger.class, 1L, 10L);
    }
  }

  public static class SequenceOfIntegersSerializer
      extends NestedSequenceOfSerializer<SequenceOfIntegers> {
    protected SequenceOfIntegersSerializer() {
      super(SequenceOfIntegers.class, "INTEGER");
    }
  }

  public static class SequenceOfIntegersDeserializer
      extends NestedSequenceOfDeserializer<SequenceOfIntegers> {
    protected SequenceOfIntegersDeserializer() {
      super(SequenceOfIntegers.class, "INTEGER");
    }
  }

  @JsonInclude(Include.NON_NULL)
  public static class SequenceOfSequences extends Asn1SequenceOf<ASequence> {
    public SequenceOfSequences() {
      super(ASequence.class, 1L, 10L);
    }
  }

  public static class SequenceOfSequencesSerializer
      extends NestedSequenceOfSerializer<SequenceOfSequences> {
    protected SequenceOfSequencesSerializer() {
      super(SequenceOfSequences.class, "SEQUENCE");
    }
  }

  public static class SequenceOfSequencesDeserializer
      extends NestedSequenceOfDeserializer<SequenceOfSequences> {
    protected SequenceOfSequencesDeserializer() {
      super(SequenceOfSequences.class, "SEQUENCE");
    }
  }
}
