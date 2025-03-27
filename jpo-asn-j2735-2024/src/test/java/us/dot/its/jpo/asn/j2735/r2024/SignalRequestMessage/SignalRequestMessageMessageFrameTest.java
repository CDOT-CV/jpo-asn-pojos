package us.dot.its.jpo.asn.j2735.r2024.SignalRequestMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import us.dot.its.jpo.asn.j2735.r2024.BaseSerializeTest;
import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the SignalRequestMessageMessageFrame class.
 */
public class SignalRequestMessageMessageFrameTest extends BaseSerializeTest<SignalRequestMessageMessageFrame> {

  public SignalRequestMessageMessageFrameTest() {
    super(SignalRequestMessageMessageFrame.class);
  }

  @Test
  public void xmlDeserialize_generatedXml() throws IOException {
    final String xml = loadResource("/us/dot/its/jpo/asn/j2735/r2024/SignalRequestMessage/xml/message_frame/srm_mf.xml");
    SignalRequestMessageMessageFrame srmmf = fromXml(xml);
    assertThat(srmmf, notNullValue());
    final String roundTripXml = toXml(srmmf);
    assertThat(roundTripXml, isIdenticalTo(xml).ignoreWhitespace().ignoreElementContentWhitespace());
  }
}
