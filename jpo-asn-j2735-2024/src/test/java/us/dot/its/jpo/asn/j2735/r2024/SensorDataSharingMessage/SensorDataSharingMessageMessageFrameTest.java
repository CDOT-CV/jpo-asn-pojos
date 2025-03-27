package us.dot.its.jpo.asn.j2735.r2024.SensorDataSharingMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import us.dot.its.jpo.asn.j2735.r2024.BaseSerializeTest;
import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the SensorDataSharingMessageMessageFrame class.
 */
public class SensorDataSharingMessageMessageFrameTest extends BaseSerializeTest<SensorDataSharingMessageMessageFrame> {
  
  public SensorDataSharingMessageMessageFrameTest() {
    super(SensorDataSharingMessageMessageFrame.class);
  }

  @Test
  public void xmlDeserialize_generatedXml() throws IOException {
    final String xml = loadResource("/us/dot/its/jpo/asn/j2735/r2024/SensorDataSharingMessage/xml/message_frame/sdsm_mf.xml");
    SensorDataSharingMessageMessageFrame sdsmmf = fromXml(xml);
    assertThat(sdsmmf, notNullValue());
    final String roundTripXml = toXml(sdsmmf);
    assertThat(roundTripXml, isIdenticalTo(xml).ignoreWhitespace().ignoreElementContentWhitespace());
  }
}
