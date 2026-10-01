package us.dot.its.jpo.asn.runtime.examples;

import com.fasterxml.jackson.annotation.JsonCreator;
import us.dot.its.jpo.asn.runtime.types.Asn1Bitstring;

/**
 * Example of a variable size BIT STRING with no named bits
 */
public class ExampleBitstringVariableSize extends Asn1Bitstring {

  @JsonCreator
  public ExampleBitstringVariableSize() {
    super(1, 16, false, new String[] {});
  }
}
