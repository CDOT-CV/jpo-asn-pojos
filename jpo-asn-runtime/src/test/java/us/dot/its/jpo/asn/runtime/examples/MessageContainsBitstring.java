package us.dot.its.jpo.asn.runtime.examples;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class MessageContainsBitstring extends Asn1Sequence {

  @JsonProperty("bits")
  private ExampleBitstringVariableSize bits;
}
