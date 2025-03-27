package us.dot.its.jpo.asn.j2735.r2024.RoadSafetyMessage;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

import us.dot.its.jpo.asn.j2735.r2024.BaseSerializeTest;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.ElementSelectors;


public class RoadSafetyMessageFrameTest extends BaseSerializeTest<RoadSafetyMessageMessageFrame> {

  public RoadSafetyMessageFrameTest() {
    super(RoadSafetyMessageMessageFrame.class);
  }

  @Test
  public void xmlDeserialize_LenientTest() throws IOException {
    final String xml = loadResource("/us/dot/its/jpo/asn/j2735/r2024/RoadSafetyMessage/xml/message_frame/rsmf_1.xml");
    RoadSafetyMessageMessageFrame rsmf = fromXml(xml);
    assertThat(rsmf, notNullValue());
    final String roundTripXml = toXml(rsmf);
    assertThat("Lenient test: Ignore order of sequence-of mixed types",
        roundTripXml,
        isSimilarTo(xml).ignoreWhitespace().normalizeWhitespace()
            .withNodeMatcher(new DefaultNodeMatcher(
                ElementSelectors.byNameAndText
            )));
  }

  @Test
  public void xmlDeserialize_StrictTest() throws IOException {
    final String xml = loadResource("/us/dot/its/jpo/asn/j2735/r2024/RoadSafetyMessage/xml/message_frame/rsmf_1.xml");
    RoadSafetyMessageMessageFrame rsmf = fromXml(xml);
    assertThat(rsmf, notNullValue());
    final String roundTripXml = toXml(rsmf);
    assertThat("strict test: exact element order", roundTripXml,
        isIdenticalTo(xml).ignoreComments().ignoreWhitespace());
  }
}
