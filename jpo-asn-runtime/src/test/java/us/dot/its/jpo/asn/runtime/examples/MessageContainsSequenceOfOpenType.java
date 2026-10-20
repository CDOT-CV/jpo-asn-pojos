package us.dot.its.jpo.asn.runtime.examples;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"id", "items"})
@Getter
@Setter
public class MessageContainsSequenceOfOpenType extends Asn1Sequence {

  @JsonProperty("id")
  private AInteger id;

  @JsonProperty("items")
  @JsonSerialize(using = SequenceOfOpenType.SequenceOfOpenTypeTestSerializer.class)
  private SequenceOfOpenType items;
}
