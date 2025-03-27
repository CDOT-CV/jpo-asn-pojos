package us.dot.its.jpo.asn.j2735.r2024.SPAT;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import us.dot.its.jpo.asn.j2735.r2024.BaseSerializeTest;
import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the SPATMessageFrame class.
 */
public class SPATMessageFrameTest extends BaseSerializeTest<SPATMessageFrame> {

    public SPATMessageFrameTest() {
        super(SPATMessageFrame.class);
    }

    @Test
    public void xmlDeserialize_generatedXml() throws IOException {
        final String xml = loadResource("/us/dot/its/jpo/asn/j2735/r2024/SPAT/xml/message_frame/spat_mf.xml");
        SPATMessageFrame spatmf = fromXml(xml);
        assertThat(spatmf, notNullValue());
        final String roundTripXml = toXml(spatmf);
        assertThat(roundTripXml, isIdenticalTo(xml).ignoreWhitespace().ignoreElementContentWhitespace());
    }
}
