package us.dot.its.jpo.asn.j2735.r2024.BasicSafetyMessage;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import us.dot.its.jpo.asn.j2735.r2024.BaseSerializeTest;
import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class BasicSafetyMessageMessageFrameTest extends
    BaseSerializeTest<BasicSafetyMessageMessageFrame> {

  public BasicSafetyMessageMessageFrameTest() {
    super(BasicSafetyMessageMessageFrame.class);
  }

  @ParameterizedTest
  @MethodSource("getXmlResourcesMessageFrame")
  public void xmlRoundTripTest(String resourcePath) throws IOException {
    final String xml = loadResource(resourcePath);
    BasicSafetyMessageMessageFrame bsmf = fromXml(xml);
    assertThat(bsmf, notNullValue());
    final String roundTripXml = toXml(bsmf);
    assertThat(roundTripXml,
        isIdenticalTo(xml).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @ParameterizedTest
  @MethodSource("getJsonResourcesMessageFrame")
  public void jsonRoundTripTest(String resourcePath) throws IOException {
    final String json = loadResource(resourcePath);
    BasicSafetyMessageMessageFrame bsmf = fromJson(json);
    assertThat(bsmf, notNullValue());
    final String roundTripJson = toJson(bsmf);
    assertThat(roundTripJson, jsonEquals(json));
  }

  private static Stream<Arguments> getXmlResourcesMessageFrame() {
    return getResources("/us/dot/its/jpo/asn/j2735/r2024/BasicSafetyMessage/xml/message_frame");
  }

  private static Stream<Arguments> getJsonResourcesMessageFrame() {
    return getResources("/us/dot/its/jpo/asn/j2735/r2024/BasicSafetyMessage/json/message_frame");
  }
}
