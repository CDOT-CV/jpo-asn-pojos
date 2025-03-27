package us.dot.its.jpo.asn.j2735.r2024.MapData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import us.dot.its.jpo.asn.j2735.r2024.BaseBitstringTest;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LaneAttributes_BikeTest extends BaseBitstringTest<LaneAttributes_Bike> {

  public static Stream<Arguments> getTestValues() {
    return testValues(new String[]{
        "1111111000000000",
        "1111000000000000",
        "0000111000000000",
        "0000000000000000"
    });
  }

  @ParameterizedTest
  @MethodSource("getTestValues")
  public void testGetters(String str) {
    var la = new LaneAttributes_Bike();
    la.fromBinaryString(str);
    assertThat(la.isBikeRevocableLane(), equalTo(bit(0, str)));
    assertThat(la.isBikeRevocableLane(), equalTo(bit(0, str)));
    assertThat(la.isPedestrianUseAllowed(), equalTo(bit(1, str)));
    assertThat(la.isIsBikeFlyOverLane(), equalTo(bit(2, str)));
    assertThat(la.isFixedCycleTime(), equalTo(bit(3, str)));
    assertThat(la.isBiDirectionalCycleTimes(), equalTo(bit(4, str)));
    assertThat(la.isIsolatedByBarrier(), equalTo(bit(5, str)));
    assertThat(la.isUnsignalizedSegmentsPresent(), equalTo(bit(6, str)));
  }

  @ParameterizedTest
  @MethodSource("getTestValues")
  public void testSetters(String str) {
    var la = new LaneAttributes_Bike();
    la.setBikeRevocableLane(bit(0, str));
    la.setPedestrianUseAllowed(bit(1, str));
    la.setIsBikeFlyOverLane(bit(2, str));
    la.setFixedCycleTime(bit(3, str));
    la.setBiDirectionalCycleTimes(bit(4, str));
    la.setIsolatedByBarrier(bit(5, str));
    la.setUnsignalizedSegmentsPresent(bit(6, str));
    assertThat(la.binaryString(), equalTo(str));
  }

}
