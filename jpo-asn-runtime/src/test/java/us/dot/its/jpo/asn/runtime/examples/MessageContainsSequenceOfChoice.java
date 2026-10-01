package us.dot.its.jpo.asn.runtime.examples;

import us.dot.its.jpo.asn.runtime.annotations.Asn1Property;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString(callSuper = true)
public class MessageContainsSequenceOfChoice extends Asn1Sequence {

  @Asn1Property(tag = 0, name = "id")
  @JsonProperty("id")
  private ExampleInteger id;

  @Asn1Property(tag = 1, name = "choices")
  @JsonProperty("choices")
  @JsonSerialize(using = SequenceOfChoice.TestSequenceOfChoiceSerializer.class)
  @JsonDeserialize(using = SequenceOfChoice.TestSequenceOfChoiceDeserializer.class)
  private SequenceOfChoice choices;

  @Asn1Property(tag = 2, name = "num")
  @JsonProperty("num")
  private ExampleInteger num;

  public MessageContainsSequenceOfChoice() {
  }

  @Override
  public boolean hasExtensionMarker() {
    return true;
  }
}
