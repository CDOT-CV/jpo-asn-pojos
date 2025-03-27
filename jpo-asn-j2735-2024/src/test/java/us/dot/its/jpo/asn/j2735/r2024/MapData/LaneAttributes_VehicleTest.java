package us.dot.its.jpo.asn.j2735.r2024.MapData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import us.dot.its.jpo.asn.j2735.r2024.BaseBitstringTest;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LaneAttributes_VehicleTest extends BaseBitstringTest<LaneAttributes_Vehicle> {

  public static Stream<Arguments> getTestValues() {
    return testValues(new String[]{
        "11111111",
        "11110000",
        "00001111",
        "00000000"
    });
  }

  @ParameterizedTest
  @MethodSource("getTestValues")
  public void testGetters(String str) {
    var la = new LaneAttributes_Vehicle();
    la.fromBinaryString(str);
    assertThat(la.isIsVehicleRevocableLane(), equalTo(bit(0, str)));
    assertThat(la.isIsVehicleFlyOverLane(), equalTo(bit(1, str)));
    assertThat(la.isHovLaneUseOnly(), equalTo(bit(2, str)));
    assertThat(la.isRestrictedToBusUse(), equalTo(bit(3, str)));
    assertThat(la.isRestrictedToTaxiUse(), equalTo(bit(4, str)));
    assertThat(la.isRestrictedFromPublicUse(), equalTo(bit(5, str)));
    assertThat(la.isHasIRbeaconCoverage(), equalTo(bit(6, str)));
    assertThat(la.isPermissionOnRequest(), equalTo(bit(7, str)));
  }

  @ParameterizedTest
  @MethodSource("getTestValues")
  public void testSetters(String str) {
    var la = new LaneAttributes_Vehicle();
    la.setIsVehicleRevocableLane(bit(0, str));
    la.setIsVehicleFlyOverLane(bit(1, str));
    la.setHovLaneUseOnly(bit(2, str));
    la.setRestrictedToBusUse(bit(3, str));
    la.setRestrictedToTaxiUse(bit(4, str));
    la.setRestrictedFromPublicUse(bit(5, str));
    la.setHasIRbeaconCoverage(bit(6, str));
    la.setPermissionOnRequest(bit(7, str));
    assertThat(la.binaryString(), equalTo(str));
  }

}
