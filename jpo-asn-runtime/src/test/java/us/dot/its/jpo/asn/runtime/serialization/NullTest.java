package us.dot.its.jpo.asn.runtime.serialization;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import us.dot.its.jpo.asn.runtime.BaseSerializeTest;
import us.dot.its.jpo.asn.runtime.examples.MessageContainsNull;
import us.dot.its.jpo.asn.runtime.types.Asn1Null;

public class NullTest extends BaseSerializeTest<MessageContainsNull> {

  public NullTest() {
    super(MessageContainsNull.class);
  }

  @Test
  public void canRoundTripXml() throws IOException {
    MessageContainsNull m = fromXml(XML);
    assertThat(m.getNothing(), instanceOf(Asn1Null.class));
    assertThat(toXml(m), isIdenticalTo(XML).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @Test
  public void canRoundTripJson() throws IOException {
    MessageContainsNull m = fromJson(JSON);
    assertThat(m.getNothing(), instanceOf(Asn1Null.class));
    assertThat(toJson(m), jsonEquals(JSON));
  }

  static final String XML = """
      <MessageContainsNull>
        <id>1</id>
        <nothing/>
      </MessageContainsNull>
      """;

  static final String JSON = """
      {
        "id": 1,
        "nothing": null
      }
      """;
}
