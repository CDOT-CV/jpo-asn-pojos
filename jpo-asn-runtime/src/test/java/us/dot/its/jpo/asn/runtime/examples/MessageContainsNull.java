package us.dot.its.jpo.asn.runtime.examples;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import us.dot.its.jpo.asn.runtime.types.Asn1Null;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"id", "nothing"})
@Getter
@Setter
public class MessageContainsNull extends Asn1Sequence {

  @JsonProperty("id")
  private AInteger id;

  @JsonProperty("nothing")
  private Asn1Null nothing;
}
