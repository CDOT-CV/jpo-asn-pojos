package us.dot.its.jpo.asn.j2735.r2024.BasicSafetyMessage;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import us.dot.its.jpo.asn.j2735.r2024.BaseSerializeTest;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class DisabledVehicleTest extends BaseSerializeTest<DisabledVehicle> {

  public DisabledVehicleTest() {
    super(DisabledVehicle.class);
  }

  @Test
  public void xmlRoundTripTest() throws IOException {
    DisabledVehicle dv = fromXml(XER);
    assertThat(dv, notNullValue());
    String roundTripXml = toXml(dv);
    assertThat(roundTripXml, isIdenticalTo(XER).ignoreWhitespace());
  }

  @Test
  public void jsonRoundTripTest() throws IOException {
    DisabledVehicle dv = fromJson(JER);
    assertThat(dv, notNullValue());
    String roundTripJson = toJson(dv);
    assertThat(roundTripJson, jsonEquals(JER));
  }


  public static final String XER = """
      <DisabledVehicle>
          <statusDetails>523</statusDetails>
          <locationDetails><traffic-circle/></locationDetails>
      </DisabledVehicle>
      """;

  public static final String JER = """
      {
          "statusDetails": 541,
          "locationDetails": "on-the-left"
      }
      """;
}
