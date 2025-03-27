package us.dot.its.jpo.asn.j2735.r2024.MapData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import us.dot.its.jpo.asn.j2735.r2024.BaseBitstringTest;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LaneDirectionTest extends BaseBitstringTest<LaneDirection> {

  public static Stream<Arguments> getTestValues() {
    return testValues(new String[] {
        "11",
        "00",
        "10",
        "01"
    });
  }

  @ParameterizedTest
  @MethodSource("getTestValues")
  public void testGetters(String str) {
    var ld = new LaneDirection();
    ld.fromBinaryString(str);
    assertThat(ld.isIngressPath(), equalTo(bit(0, str)));
    assertThat(ld.isEgressPath(), equalTo(bit(1, str)));
  }

  @ParameterizedTest
  @MethodSource("getTestValues")
  public void testSetters(String str) {
    var ld = new LaneDirection();
    ld.setIngressPath(bit(0, str));
    ld.setEgressPath(bit(1, str));
    assertThat(ld.binaryString(), equalTo(str));
  }
}
