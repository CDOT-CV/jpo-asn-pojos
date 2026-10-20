package us.dot.its.jpo.asn.runtime.examples;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;
import us.dot.its.jpo.asn.runtime.types.Asn1SequenceOf;

/**
 * SEQUENCE containing SEQUENCE OF properties of named types, which rely on the standard Jackson XML
 * wrapper annotations instead of a custom serializer, modeled after the generated J2735
 * BasicSafetyMessage "regional" property.
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"sequences", "integers"})
@Getter
@Setter
public class MessageContainsWrappedSequenceOf extends Asn1Sequence {

  @JsonProperty("sequences")
  @JacksonXmlElementWrapper(localName = "sequences")
  @JacksonXmlProperty(localName = "A-Sequence")
  private SequenceOfASequence sequences;

  @JsonProperty("integers")
  @JacksonXmlElementWrapper(localName = "integers")
  @JacksonXmlProperty(localName = "A-Integer")
  private SequenceOfAInteger integers;

  @JsonInclude(Include.NON_NULL)
  public static class SequenceOfASequence extends Asn1SequenceOf<ASequence> {
    public SequenceOfASequence() {
      super(ASequence.class, 1L, 4L);
    }
  }

  @JsonInclude(Include.NON_NULL)
  public static class SequenceOfAInteger extends Asn1SequenceOf<AInteger> {
    public SequenceOfAInteger() {
      super(AInteger.class, 1L, 4L);
    }
  }
}
