package us.dot.its.jpo.asn.j2735.r2024.MapData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import us.dot.its.jpo.asn.j2735.r2024.BaseBitstringTest;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class AllowedManeuversTest extends BaseBitstringTest<AllowedManeuvers> {

  private static Stream<Arguments> getTestValues() {
    return testValues(new String[] {
        "111111111111",
        "000000000000",
        "111111000000",
        "000000111111"
    });
  }

  @ParameterizedTest
  @MethodSource("getTestValues")
  public void testGetters(String str) {
    var am = new AllowedManeuvers();
    am.fromBinaryString(str);
    assertThat(am.isManeuverStraightAllowed(), equalTo(bit(0, str)));
    assertThat(am.isManeuverLeftAllowed(), equalTo(bit(1, str)));
    assertThat(am.isManeuverRightAllowed(), equalTo(bit(2, str)));
    assertThat(am.isManeuverUTurnAllowed(), equalTo(bit(3, str)));
    assertThat(am.isManeuverLeftTurnOnRedAllowed(), equalTo(bit(4, str)));
    assertThat(am.isManeuverRightTurnOnRedAllowed(), equalTo(bit(5, str)));
    assertThat(am.isManeuverLaneChangeAllowed(), equalTo(bit(6, str)));
    assertThat(am.isManeuverNoStoppingAllowed(), equalTo(bit(7, str)));
    assertThat(am.isYieldAllwaysRequired(), equalTo(bit(8, str)));
    assertThat(am.isGoWithHalt(), equalTo(bit(9, str)));
    assertThat(am.isCaution(), equalTo(bit(10, str)));
    assertThat(am.isReserved1(), equalTo(bit(11, str)));
  }

  @ParameterizedTest
  @MethodSource("getTestValues")
  public void testSetters(String str) {
    var am = new AllowedManeuvers();
    am.setManeuverStraightAllowed(bit(0, str));
    am.setManeuverLeftAllowed(bit(1, str));
    am.setManeuverRightAllowed(bit(2, str));
    am.setManeuverUTurnAllowed(bit(3, str));
    am.setManeuverLeftTurnOnRedAllowed(bit(4, str));
    am.setManeuverRightTurnOnRedAllowed(bit(5, str));
    am.setManeuverLaneChangeAllowed(bit(6, str));
    am.setManeuverNoStoppingAllowed(bit(7, str));
    am.setYieldAllwaysRequired(bit(8, str));
    am.setGoWithHalt(bit(9, str));
    am.setCaution(bit(10, str));
    am.setReserved1(bit(11, str));
    assertThat(am.binaryString(), equalTo(str));
  }
}


