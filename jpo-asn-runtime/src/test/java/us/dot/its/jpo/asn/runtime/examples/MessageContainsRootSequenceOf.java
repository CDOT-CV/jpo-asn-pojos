package us.dot.its.jpo.asn.runtime.examples;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import us.dot.its.jpo.asn.runtime.serialization.RootSequenceOfSerializer;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;
import us.dot.its.jpo.asn.runtime.types.Asn1SequenceOf;

/**
 * SEQUENCE with two SEQUENCE OF properties having the same item type, which requires the
 * {@link RootSequenceOfSerializer}.  Each list class can also be serialized as a root document.
 */
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"first", "second"})
@Getter
@Setter
public class MessageContainsRootSequenceOf extends Asn1Sequence {

  @JsonProperty("first")
  private FirstList first;

  @JsonProperty("second")
  private SecondList second;

  @JsonSerialize(using = RootSequenceOfSerializer.class)
  @JsonRootName("first")
  public static class FirstList extends Asn1SequenceOf<ASequence> {
    public FirstList() {
      super(ASequence.class, 1L, 10L);
    }
  }

  @JsonSerialize(using = RootSequenceOfSerializer.class)
  @JsonRootName("second")
  public static class SecondList extends Asn1SequenceOf<ASequence> {
    public SecondList() {
      super(ASequence.class, 1L, 10L);
    }
  }
}
