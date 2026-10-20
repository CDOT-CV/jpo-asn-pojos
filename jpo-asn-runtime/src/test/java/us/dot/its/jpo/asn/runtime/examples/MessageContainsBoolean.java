package us.dot.its.jpo.asn.runtime.examples;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import us.dot.its.jpo.asn.runtime.types.Asn1Boolean;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"flag", "example"})
@Getter
@Setter
public class MessageContainsBoolean extends Asn1Sequence {

  @JsonProperty("flag")
  private Asn1Boolean flag;

  @JsonProperty("example")
  private ExampleBoolean example;
}
