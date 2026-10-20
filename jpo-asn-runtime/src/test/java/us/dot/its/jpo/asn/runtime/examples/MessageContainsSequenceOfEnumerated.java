package us.dot.its.jpo.asn.runtime.examples;

import lombok.ToString;
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

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString(callSuper = true)
public class MessageContainsSequenceOfEnumerated extends Asn1Sequence {

  @Asn1Property(tag = 0, name = "id")
  @JsonProperty("id")
  private ExampleInteger id;

  @Asn1Property(tag = 1, name = "fruits")
  @JsonProperty("fruits")
  @JsonSerialize(using = SequenceOfFruitEnum.SequenceOfFruitEnumSerializer.class)
  @JsonDeserialize(using = SequenceOfFruitEnum.SequenceOfFruitEnumDeserializer.class)
  private SequenceOfFruitEnum fruits;

  public MessageContainsSequenceOfEnumerated() {
  }

  @Override
  public boolean hasExtensionMarker() {
    return true;
  }
}
