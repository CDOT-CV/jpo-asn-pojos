package us.dot.its.jpo.asn.j2735.r2024.Common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class VehicleEventFlagsTest {

  @Test
  public void testGetters() {
    var vef = new VehicleEventFlags();
    vef.fromBinaryString("1111111111111");
    assertThat(vef.isEventHazardLights(), equalTo(true));
    assertThat(vef.isEventStopLineViolation(), equalTo(true));
    assertThat(vef.isEventABSactivated(), equalTo(true));
    assertThat(vef.isEventTractionControlLoss(), equalTo(true));
    assertThat(vef.isEventStabilityControlactivated(), equalTo(true));
    assertThat(vef.isEventHazardousMaterials(), equalTo(true));
    assertThat(vef.isEventReserved1(), equalTo(true));
    assertThat(vef.isEventHardBraking(), equalTo(true));
    assertThat(vef.isEventLightsChanged(), equalTo(true));
    assertThat(vef.isEventWipersChanged(), equalTo(true));
    assertThat(vef.isEventFlatTire(), equalTo(true));
    assertThat(vef.isEventDisabledVehicle(), equalTo(true));
    assertThat(vef.isEventAirBagDeployment(), equalTo(true));
  }

  @Test
  public void testSetters() {
    var vef = new VehicleEventFlags();
    vef.setEventHazardLights(true);
    vef.setEventStopLineViolation(true);
    vef.setEventABSactivated(true);
    vef.setEventTractionControlLoss(true);
    vef.setEventStabilityControlactivated(true);
    vef.setEventHazardousMaterials(true);
    vef.setEventReserved1(true);
    vef.setEventHardBraking(true);
    vef.setEventLightsChanged(true);
    vef.setEventWipersChanged(true);
    vef.setEventFlatTire(true);
    vef.setEventDisabledVehicle(true);
    vef.setEventAirBagDeployment(true);
    assertThat(vef.binaryString(), equalTo("1111111111111"));
  }

  @ParameterizedTest
  @CsvSource({
      "00000000000001,true",
      "10000000000001,true",
      "11111111111111,true",
      "01010101010101,true",
      "00000000000000,false",
      "10000000000000,false",
      "1111111111111,false",
      "0101010101010,false",
      "0000000000000,false",
      "1000000000000,false",
      "1111111111111,false",
      "0101010101010,false"
  })
  public void testJackKnifeExtension_fromBinaryString(
      final String binaryString, boolean expectSetBit) {
    var vef = new VehicleEventFlags();
    vef.fromBinaryString(binaryString);
    assertThat(vef, hasProperty("eventJackKnife", equalTo(expectSetBit)));
  }

  @ParameterizedTest
  @CsvSource({
      "0004,true",
      "8004,true",
      "FFFC,true",
      "5555,true",
      "0000,false",
      "8000,false",
      "FFF8,false",
      "5550,false"
  })
  public void testJackKnifeExtension_fromHexString(final String hexString, boolean expectSetBit) {
    var vef = new VehicleEventFlags();
    vef.fromHexString(hexString);
    assertThat(vef, hasProperty("eventJackKnife", equalTo(expectSetBit)));
  }

  @Test
  public void testJackKnifeExtension_encodeBinaryString() {
    var vef = new VehicleEventFlags();
    vef.setEventHazardLights(true);
    vef.setEventJackKnife(true);
    final String binaryStr = vef.binaryString();
    assertThat(binaryStr, equalTo("10000000000001"));
    vef.setEventJackKnife(false);
    final String binaryStrZero = vef.binaryString();
    assertThat(binaryStrZero, equalTo("1000000000000"));
  }

  @Test
  public void testJackKnifeExtension_encodeHexString() {
    var vef = new VehicleEventFlags();
    vef.setEventHazardLights(true);
    vef.setEventJackKnife(true);
    final String binaryStr = vef.hexString();
    assertThat(binaryStr, equalTo("8004"));
    vef.setEventJackKnife(false);
    final String binaryStrZero = vef.hexString();
    assertThat(binaryStrZero, equalTo("8000"));
  }



}
