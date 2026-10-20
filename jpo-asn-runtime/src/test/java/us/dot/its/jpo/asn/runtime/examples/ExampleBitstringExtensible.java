package us.dot.its.jpo.asn.runtime.examples;

import com.fasterxml.jackson.annotation.JsonCreator;
import us.dot.its.jpo.asn.runtime.types.Asn1Bitstring;

/**
 * Example of an extensible BIT STRING with named bits
 */
public class ExampleBitstringExtensible extends Asn1Bitstring {

  @JsonCreator
  public ExampleBitstringExtensible() {
    super(4, true, new String[] {"a", "b", "c", "d"});
  }
}
