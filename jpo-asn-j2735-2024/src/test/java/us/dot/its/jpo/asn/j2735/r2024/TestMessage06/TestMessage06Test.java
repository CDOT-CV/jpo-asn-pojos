package us.dot.its.jpo.asn.j2735.r2024.TestMessage06;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import us.dot.its.jpo.asn.j2735.r2024.BaseSerializeTest;

public class TestMessage06Test extends BaseSerializeTest<TestMessage06> {

  public TestMessage06Test() {
    super(TestMessage06.class);
  }

  @ParameterizedTest
  @MethodSource("getXmlResources")
  public void canRoundTripXml(String resourcePath) throws IOException {
    String xml = loadResource(resourcePath);
    var msg = fromXml(xml);
    assertThat(msg, notNullValue());
    final String roundTripXml = toXml(msg);
    assertThat(roundTripXml, isIdenticalTo(xml).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @ParameterizedTest
  @MethodSource("getJsonResources")
  public void canRoundTripJson(String resourcePath) throws IOException {
    String json = loadResource(resourcePath);
    var msg = fromJson(json);
    assertThat(msg, notNullValue());
    final String roundTripJson = toJson(msg);
    assertThat(roundTripJson, jsonEquals(json));
  }

  private static Stream<Arguments> getXmlResources() {
    return getResources("/us/dot/its/jpo/asn/j2735/r2024/TestMessage06/xml");
  }

  private static Stream<Arguments> getJsonResources() {
    return getResources("/us/dot/its/jpo/asn/j2735/r2024/TestMessage06/json");
  }
}
