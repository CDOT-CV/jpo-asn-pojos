package us.dot.its.jpo.asn.j2735.r2024.MapData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import us.dot.its.jpo.asn.j2735.r2024.BaseBitstringTest;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LaneAttributes_SidewalkTest extends BaseBitstringTest<LaneAttributes_Sidewalk> {

  public static Stream<Arguments> getTestValues() {
    return testValues(new String[]{
        "1111000000000000",
        "1100000000000000",
        "0011000000000000",
        "0000000000000000"
    });
  }

  @ParameterizedTest
  @MethodSource("getTestValues")
  public void testGetters(String str) {
    var la = new LaneAttributes_Sidewalk();
    la.fromBinaryString(str);
    assertThat(la.isSidewalk_RevocableLane(), equalTo(bit(0, str)));
    assertThat(la.isBicyleUseAllowed(), equalTo(bit(1, str)));
    assertThat(la.isIsSidewalkFlyOverLane(), equalTo(bit(2, str)));
    assertThat(la.isWalkBikes(), equalTo(bit(3, str)));
  }

  @ParameterizedTest
  @MethodSource("getTestValues")
  public void testSetters(String str) {
    var la = new LaneAttributes_Sidewalk();
    la.setSidewalk_RevocableLane(bit(0, str));
    la.setBicyleUseAllowed(bit(1, str));
    la.setIsSidewalkFlyOverLane(bit(2, str));
    la.setWalkBikes(bit(3, str));
    assertThat(la.binaryString(), equalTo(str));
  }

}
