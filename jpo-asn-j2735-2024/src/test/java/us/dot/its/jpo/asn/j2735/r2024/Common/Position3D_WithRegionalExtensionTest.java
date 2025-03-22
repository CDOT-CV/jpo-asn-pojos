package us.dot.its.jpo.asn.j2735.r2024.Common;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import us.dot.its.jpo.asn.j2735.r2024.BaseSerializeTest;

public class Position3D_WithRegionalExtensionTest
  extends BaseSerializeTest<Position3D> {

  public Position3D_WithRegionalExtensionTest() {
    super(Position3D.class);
  }

  @Test
  public void canRoundTripXml() throws IOException {
    final String xml = loadResource("/us/dot/its/jpo/asn/j2735/r2024/Common/Position3D_regional.xml");
    Position3D msg = fromXml(xml);
    assertThat("null", msg, notNullValue());
    assertThat("regional is null", msg.getRegional(), notNullValue());
    final String roundTripXml = toXml(msg);
    assertThat(roundTripXml, isIdenticalTo(xml).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @Test
  public void canRoundTripJson() throws IOException {
    final String json = loadResource("/us/dot/its/jpo/asn/j2735/r2024/Common/Position3D_regional.json");
    Position3D msg = fromJson(json);
    assertThat("null", msg, notNullValue());
    assertThat("regional field null", msg.getRegional(), notNullValue());
    final String  roundTripJson = toJson(msg);
    assertThat(roundTripJson, jsonEquals(json));
  }
}
