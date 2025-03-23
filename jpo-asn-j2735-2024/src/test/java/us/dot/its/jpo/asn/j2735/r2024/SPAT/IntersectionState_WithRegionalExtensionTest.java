package us.dot.its.jpo.asn.j2735.r2024.SPAT;


import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import us.dot.its.jpo.asn.j2735.r2024.BaseSerializeTest;

public class IntersectionState_WithRegionalExtensionTest
  extends BaseSerializeTest<IntersectionState> {

  public IntersectionState_WithRegionalExtensionTest() {
    super(IntersectionState.class);
  }

  @Test
  public void canRoundTripXml() throws IOException {
    final String xml = loadResource("/us/dot/its/jpo/asn/j2735/r2024/SPAT/regional/IntersectionState_regional.xml");
    IntersectionState msg = fromXml(xml);
    assertThat("null", msg, notNullValue());
    assertThat("regional field null", msg.getRegional(), notNullValue());
    final String roundTripXml = toXml(msg);
    assertThat(roundTripXml, isIdenticalTo(xml).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @Test
  public void canRoundTripJson() throws IOException {
    final String json = loadResource("/us/dot/its/jpo/asn/j2735/r2024/SPAT/regional/IntersectionState_regional.json");
    IntersectionState msg = fromJson(json);
    assertThat("null", msg, notNullValue());
    assertThat("regional field null", msg.getRegional(), notNullValue());
    final String  roundTripJson = toJson(msg);
    assertThat(roundTripJson, jsonEquals(json));
  }
}
