package us.dot.its.jpo.asn.j2735.r2024.TollUsageMessage;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import us.dot.its.jpo.asn.j2735.r2024.BaseSerializeTest;

public class VehicleAxlesAndWeightInfoTest extends BaseSerializeTest<VehicleAxlesAndWeightInfo> {

  public VehicleAxlesAndWeightInfoTest() {
    super(VehicleAxlesAndWeightInfo.class);
  }

  @Test
  public void canRoundTripXml() throws IOException {
    String xml = loadResource("/us/dot/its/jpo/asn/j2735/r2024/TollUsageMessage/VehicleAxlesAndWeightInfo/VehicleAxlesAndWeightInfo_rt.xml");
    var msg = fromXml(xml);
    assertThat(msg, notNullValue());
    final String roundTripXml = toXml(msg);
    assertThat(roundTripXml, isIdenticalTo(xml).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @Test
  public void canRoundTripJson() throws IOException {
    String json = loadResource("/us/dot/its/jpo/asn/j2735/r2024/TollUsageMessage/VehicleAxlesAndWeightInfo/VehicleAxlesAndWeightInfo_rt.json");
    var msg = fromJson(json);
    assertThat(msg, notNullValue());
    final String roundTripJson = toJson(msg);
    assertThat(roundTripJson, jsonEquals(json));
  }
}
