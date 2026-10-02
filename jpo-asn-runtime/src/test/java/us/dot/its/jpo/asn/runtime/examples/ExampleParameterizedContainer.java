package us.dot.its.jpo.asn.runtime.examples;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;

/**
 * SEQUENCE containing a parameterized type followed by another field, to verify the parameterized
 * type deserializer leaves the parser positioned correctly.
 */
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"before", "frame", "after"})
@Getter
@Setter
public class ExampleParameterizedContainer extends Asn1Sequence {

  @JsonProperty("before")
  private AInteger before;

  @SuppressWarnings("rawtypes")
  @JsonProperty("frame")
  private ExampleParameterized frame;

  @JsonProperty("after")
  private AInteger after;
}
