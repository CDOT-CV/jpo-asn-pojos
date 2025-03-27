package us.dot.its.jpo.asn.j2735.r2024.MapData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import us.dot.its.jpo.asn.j2735.r2024.BaseBitstringTest;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LaneAttributes_StripingTest extends BaseBitstringTest<LaneAttributes_Striping> {


  public static Stream<Arguments> getTestValues() {
    return testValues(new String[]{
        "1111110000000000",
        "1110000000000000",
        "0001110000000000",
        "0000000000000000"
    });
  }

  @ParameterizedTest
  @MethodSource("getTestValues")
  public void testGetters(String str) {
    var la = new LaneAttributes_Striping();
    la.fromBinaryString(str);
    assertThat(la.isStripeToConnectingLanesRevocableLane(), equalTo(bit(0, str)));
    assertThat(la.isStripeDrawOnLeft(), equalTo(bit(1, str)));
    assertThat(la.isStripeDrawOnRight(), equalTo(bit(2, str)));
    assertThat(la.isStripeToConnectingLanesLeft(), equalTo(bit(3, str)));
    assertThat(la.isStripeToConnectingLanesRight(), equalTo(bit(4, str)));
    assertThat(la.isStripeToConnectingLanesAhead(), equalTo(bit(5, str)));
  }

  @ParameterizedTest
  @MethodSource("getTestValues")
  public void testSetters(String str) {
    var la = new LaneAttributes_Striping();
    la.setStripeToConnectingLanesRevocableLane(bit(0, str));
    la.setStripeDrawOnLeft(bit(1, str));
    la.setStripeDrawOnRight(bit(2, str));
    la.setStripeToConnectingLanesLeft(bit(3, str));
    la.setStripeToConnectingLanesRight(bit(4, str));
    la.setStripeToConnectingLanesAhead(bit(5, str));
    assertThat(la.binaryString(), equalTo(str));
  }
}
