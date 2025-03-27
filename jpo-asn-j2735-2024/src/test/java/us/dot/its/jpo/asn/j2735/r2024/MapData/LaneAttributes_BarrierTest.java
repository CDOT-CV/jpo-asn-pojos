package us.dot.its.jpo.asn.j2735.r2024.MapData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import us.dot.its.jpo.asn.j2735.r2024.BaseBitstringTest;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LaneAttributes_BarrierTest extends BaseBitstringTest<LaneAttributes_Barrier> {

  public static Stream<Arguments> getTestValues() {
    return testValues(new String[]{
        "1111111111000000",
        "1111100000000000",
        "0000011110000000",
        "0000000000000000"
    });
  }

  @ParameterizedTest
  @MethodSource("getTestValues")
  public void testGetters(String str) {
    var la = new LaneAttributes_Barrier();
    la.fromBinaryString(str);
    assertThat(la.isMedian_RevocableLane(), equalTo(bit(0, str)));
    assertThat(la.isMedian(), equalTo(bit(1, str)));
    assertThat(la.isWhiteLineHashing(), equalTo(bit(2, str)));
    assertThat(la.isStripedLines(), equalTo(bit(3, str)));
    assertThat(la.isDoubleStripedLines(), equalTo(bit(4, str)));
    assertThat(la.isTrafficCones(), equalTo(bit(5, str)));
    assertThat(la.isConstructionBarrier(), equalTo(bit(6, str)));
    assertThat(la.isTrafficChannels(), equalTo(bit(7, str)));
    assertThat(la.isLowCurbs(), equalTo(bit(8, str)));
    assertThat(la.isHighCurbs(), equalTo(bit(9, str)));
  }

  @ParameterizedTest
  @MethodSource("getTestValues")
  public void testSetters(String str) {
    var la = new LaneAttributes_Barrier();
    la.setMedian_RevocableLane(bit(0, str));
    la.setMedian(bit(1, str));
    la.setWhiteLineHashing(bit(2, str));
    la.setStripedLines(bit(3, str));
    la.setDoubleStripedLines(bit(4, str));
    la.setTrafficCones(bit(5, str));
    la.setConstructionBarrier(bit(6, str));
    la.setTrafficChannels(bit(7, str));
    la.setLowCurbs(bit(8, str));
    la.setHighCurbs(bit(9, str));
    assertThat(la.binaryString(), equalTo(str));
  }

}
