package us.dot.its.jpo.asn.j2735.r2024.MapData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import us.dot.its.jpo.asn.j2735.r2024.BaseBitstringTest;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LaneAttributes_ParkingTest extends BaseBitstringTest<LaneAttributes_Parking> {
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
    var la = new LaneAttributes_Parking();
    la.fromBinaryString(str);
    assertThat(la.isParkingRevocableLane(), equalTo(bit(0, str)));
    assertThat(la.isParallelParkingInUse(), equalTo(bit(1, str)));
    assertThat(la.isHeadInParkingInUse(), equalTo(bit(2, str)));
    assertThat(la.isDoNotParkZone(), equalTo(bit(3, str)));
    assertThat(la.isParkingForBusUse(), equalTo(bit(4, str)));
    assertThat(la.isParkingForTaxiUse(), equalTo(bit(5, str)));
    assertThat(la.isNoPublicParkingUse(), equalTo(bit(6, str)));
  }

  @ParameterizedTest
  @MethodSource("getTestValues")
  public void testSetters(String str) {
    var la = new LaneAttributes_Parking();
    la.setParkingRevocableLane(bit(0, str));
    la.setParallelParkingInUse(bit(1, str));
    la.setHeadInParkingInUse(bit(2, str));
    la.setDoNotParkZone(bit(3, str));
    la.setParkingForBusUse(bit(4, str));
    la.setParkingForTaxiUse(bit(5, str));
    la.setNoPublicParkingUse(bit(6, str));
    assertThat(la.binaryString(), equalTo(str));
  }
}
