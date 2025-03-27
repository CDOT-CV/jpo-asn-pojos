package us.dot.its.jpo.asn.j2735.r2024;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import us.dot.its.jpo.asn.runtime.types.Asn1Bitstring;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public abstract class BaseBitstringTest<T extends Asn1Bitstring> {

  protected boolean bit(int num, String str) {
    char c = str.charAt(num);
    if (c == '1')
      return true;
    else if (c == '0')
      return false;
    else
      throw new RuntimeException("Invalid bit character: " + c);
  }

  protected static Stream<Arguments> testValues(String[] testValues) {
    var sb = Stream.<Arguments>builder();
    for (String str : testValues) {
      sb.add(arguments(str));
    }
    return sb.build();
  }
}
