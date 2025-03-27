package us.dot.its.jpo.asn.j2735.r2024.MapData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import us.dot.its.jpo.asn.j2735.r2024.BaseBitstringTest;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LaneAttributes_TrackedVehicleTest extends BaseBitstringTest<LaneAttributes_TrackedVehicle> {

  public static Stream<Arguments> getTestValues() {
    return testValues(new String[]{
        "1111100000000000",
        "1111100000000000",
        "1110000000000000",
        "0001100000000000"
    });
  }

  @ParameterizedTest
  @MethodSource("getTestValues")
  public void testGetters(String str) {
    var la = new LaneAttributes_TrackedVehicle();
    la.fromBinaryString(str);
    assertThat(la.isSpec_RevocableLane(), equalTo(bit(0, str)));
    assertThat(la.isSpec_commuterRailRoadTrack(), equalTo(bit(1, str)));
    assertThat(la.isSpec_lightRailRoadTrack(), equalTo(bit(2, str)));
    assertThat(la.isSpec_heavyRailRoadTrack(), equalTo(bit(3, str)));
    assertThat(la.isSpec_otherRailType(), equalTo(bit(4, str)));
  }

  @ParameterizedTest
  @MethodSource("getTestValues")
  public void testSetters(String str) {
    var la = new LaneAttributes_TrackedVehicle();
    la.setSpec_RevocableLane(bit(0, str));
    la.setSpec_commuterRailRoadTrack(bit(1, str));
    la.setSpec_lightRailRoadTrack(bit(2, str));
    la.setSpec_heavyRailRoadTrack(bit(3, str));
    la.setSpec_otherRailType(bit(4, str));
    assertThat(la.binaryString(), equalTo(str));
  }

}
