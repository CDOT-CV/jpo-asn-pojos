package us.dot.its.jpo.asn.j2735.r2024.ProbeDataConfig;

import org.junit.jupiter.api.Test;
import us.dot.its.jpo.asn.j2735.r2024.BaseSerializeTest;
import us.dot.its.jpo.asn.j2735.r2024.MapData.MapDataMessageFrame;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

public class ProbeDataConfigMessageMessageFrameTest extends BaseSerializeTest<ProbeDataConfigMessageMessageFrame> {
    public ProbeDataConfigMessageMessageFrameTest() {
        super(ProbeDataConfigMessageMessageFrame.class);
    }

    @Test
    public void xmlRoundTripTest() throws IOException {
        final String xml = loadResource("/us/dot/its/jpo/asn/j2735/r2024/ProbeDataConfig/message_frame/pdc_mf.xml");
        var msg = fromXml(xml);

        String json = toJson(msg);
        System.out.println(json);

        assertThat(msg, notNullValue());
        final String roundTripXml = toXml(msg);
        assertThat(roundTripXml,
                isIdenticalTo(xml).ignoreWhitespace().ignoreElementContentWhitespace());
    }
}
