package us.dot.its.jpo.asn.j2735.r2024.SPAT;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import us.dot.its.jpo.asn.j2735.r2024.BaseSerializeTest;

public class MovementEvent_WithRegionalExtensionTest extends BaseSerializeTest<MovementEvent> {

  public MovementEvent_WithRegionalExtensionTest() {
    super(MovementEvent.class);
  }

  @Test
  public void canRoundTripXml() throws IOException {
    final String xml = loadResource("/us/dot/its/jpo/asn/j2735/r2024/SPAT/regional/MovementEvent_regional.xml");
    MovementEvent me = fromXml(xml);
    assertThat("null", me, notNullValue());
    assertThat("regional field null", me.getRegional(), notNullValue());
    final String roundTripXml = toXml(me);
    assertThat(roundTripXml, isIdenticalTo(xml).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @Test
  public void canRoundTripJson() throws IOException {
    final String json = loadResource("/us/dot/its/jpo/asn/j2735/r2024/SPAT/regional/MovementEvent_regional.json");
    MovementEvent me = fromJson(json);
    assertThat("null", me, notNullValue());
    assertThat("regional field null", me.getRegional(), notNullValue());
    final String  roundTripJson = toJson(me);
    assertThat(roundTripJson, jsonEquals(json));
  }


}
