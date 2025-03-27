package us.dot.its.jpo.asn.j2735.r2024.MapData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import us.dot.its.jpo.asn.j2735.r2024.BaseBitstringTest;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LaneAttributes_CrosswalkTest extends BaseBitstringTest<LaneAttributes_Crosswalk> {


  public static Stream<Arguments> getTestValues() {
    return testValues(new String[]{
        "1111111110000000",
        "1111000000000000",
        "0000111110000000",
        "0000000000000000"
    });
  }

  @ParameterizedTest
  @MethodSource("getTestValues")
  public void testGetters(String str) {
    var la = new LaneAttributes_Crosswalk();
    la.fromBinaryString(str);
    assertThat(la.isCrosswalkRevocableLane(), equalTo(bit(0, str)));
    assertThat(la.isBicyleUseAllowed(), equalTo(bit(1, str)));
    assertThat(la.isIsXwalkFlyOverLane(), equalTo(bit(2, str)));
    assertThat(la.isFixedCycleTime(), equalTo(bit(3, str)));
    assertThat(la.isBiDirectionalCycleTimes(), equalTo(bit(4, str)));
    assertThat(la.isHasPushToWalkButton(), equalTo(bit(5, str)));
    assertThat(la.isAudioSupport(), equalTo(bit(6, str)));
    assertThat(la.isRfSignalRequestPresent(), equalTo(bit(7, str)));
    assertThat(la.isUnsignalizedSegmentsPresent(), equalTo(bit(8, str)));
  }

  @ParameterizedTest
  @MethodSource("getTestValues")
  public void testSetters(String str) {
    var la = new LaneAttributes_Crosswalk();
    la.setCrosswalkRevocableLane(bit(0, str));
    la.setBicyleUseAllowed(bit(1, str));
    la.setIsXwalkFlyOverLane(bit(2, str));
    la.setFixedCycleTime(bit(3, str));
    la.setBiDirectionalCycleTimes(bit(4, str));
    la.setHasPushToWalkButton(bit(5, str));
    la.setAudioSupport(bit(6, str));
    la.setRfSignalRequestPresent(bit(7, str));
    la.setUnsignalizedSegmentsPresent(bit(8, str));
    assertThat(la.binaryString(), equalTo(str));
  }

}
