package us.dot.its.jpo.asn.j2735.r2024.MessageFrame;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;

import us.dot.its.jpo.asn.j2735.r2024.BaseSerializeTest;
import us.dot.its.jpo.asn.j2735.r2024.SPAT.SPAT;
import us.dot.its.jpo.asn.j2735.r2024.SPAT.SPATMessageFrame;
import java.io.IOException;
import org.junit.jupiter.api.Test;

// Deserialization tests for Generic MessageFrame
public class MessageFrameTest extends BaseSerializeTest<MessageFrame> {

    public MessageFrameTest() {
        super(MessageFrame.class);
    }

    @Test
    public void xmlDeserialize_SPAT() throws IOException {
        final String xml = loadResource("/us/dot/its/jpo/asn/j2735/r2024/SPAT/xml/message_frame/spat_mf.xml");
        MessageFrame<?> smf = fromXml(xml);
        assertThat(smf, notNullValue());
        assertThat(smf, instanceOf(SPATMessageFrame.class));
        assertThat(smf, hasProperty("messageId", equalTo(new DSRCmsgID(19))));
        assertThat(smf.getValue(), notNullValue());
        assertThat(smf.getValue(), instanceOf(SPAT.class));
    }

}
