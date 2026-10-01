package us.dot.its.jpo.asn.runtime.examples;

import com.fasterxml.jackson.annotation.JsonCreator;
import us.dot.its.jpo.asn.runtime.types.Asn1Bitstring;

/**
 * Example of a fixed size BIT STRING with more named bits than the size, where the named bits
 * beyond the size are extensions
 */
public class ExampleBitstringNamedExtensions extends Asn1Bitstring {

  @JsonCreator
  public ExampleBitstringNamedExtensions() {
    super(4, false, new String[] {"a", "b", "c", "d", "e", "f"});
  }
}
