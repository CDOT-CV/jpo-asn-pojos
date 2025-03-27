package us.dot.its.jpo.asn.j2735.r2024.PersonalSafetyMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import us.dot.its.jpo.asn.j2735.r2024.BaseSerializeTest;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class PersonalSafetyMessageFrameTest extends BaseSerializeTest<PersonalSafetyMessageMessageFrame> {

  public PersonalSafetyMessageFrameTest() {
    super(PersonalSafetyMessageMessageFrame.class);
  }

  @Test
  public void xmlDeserialize_generatedXml() throws IOException {
    final String xml = loadResource("/us/dot/its/jpo/asn/j2735/r2024/PersonalSafetyMessage/xml/message_frame/psm_01.xml");
    PersonalSafetyMessageMessageFrame psmf = fromXml(xml);
    assertThat(psmf, notNullValue());
    final String roundTripXml = toXml(psmf);
    assertThat(roundTripXml, isIdenticalTo(xml).ignoreWhitespace().ignoreElementContentWhitespace());
  }
}
