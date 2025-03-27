package us.dot.its.jpo.asn.j2735.r2024.MapData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import us.dot.its.jpo.asn.j2735.r2024.BaseBitstringTest;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LaneSharingTest extends BaseBitstringTest<LaneSharing> {

  private static Stream<Arguments> getTestValues() {
    return testValues(new String[] {
        "1111111111",
        "0000000000",
        "1111100000",
        "0000011111"
    });
  }

  @ParameterizedTest
  @MethodSource("getTestValues")
  public void testGetters(String str) {
    var ls = new LaneSharing();
    ls.fromBinaryString(str);
    assertThat(ls.isOverlappingLaneDescriptionProvided(), equalTo(bit(0, str)));
    assertThat(ls.isMultipleLanesTreatedAsOneLane(), equalTo(bit(1, str)));
    assertThat(ls.isOtherNonMotorizedTrafficTypes(), equalTo(bit(2, str)));
    assertThat(ls.isIndividualMotorizedVehicleTraffic(), equalTo(bit(3, str)));
    assertThat(ls.isBusVehicleTraffic(), equalTo(bit(4, str)));
    assertThat(ls.isTaxiVehicleTraffic(), equalTo(bit(5, str)));
    assertThat(ls.isPedestriansTraffic(), equalTo(bit(6, str)));
    assertThat(ls.isCyclistVehicleTraffic(), equalTo(bit(7, str)));
    assertThat(ls.isTrackedVehicleTraffic(), equalTo(bit(8, str)));
    assertThat(ls.isReserved(), equalTo(bit(9, str)));
  }

  @ParameterizedTest
  @MethodSource("getTestValues")
  public void testSetters(String str) {
    var ls = new LaneSharing();
    ls.setOverlappingLaneDescriptionProvided(bit(0, str));
    ls.setMultipleLanesTreatedAsOneLane(bit(1, str));
    ls.setOtherNonMotorizedTrafficTypes(bit(2, str));
    ls.setIndividualMotorizedVehicleTraffic(bit(3, str));
    ls.setBusVehicleTraffic(bit(4, str));
    ls.setTaxiVehicleTraffic(bit(5, str));
    ls.setPedestriansTraffic(bit(6, str));
    ls.setCyclistVehicleTraffic(bit(7, str));
    ls.setTrackedVehicleTraffic(bit(8, str));
    ls.setReserved(bit(9, str));
    assertThat(ls.binaryString(), equalTo(str));
  }
}
