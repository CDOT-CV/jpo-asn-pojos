package us.dot.its.jpo.asn.j2735.r2024.SensorDataSharingMessage;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import us.dot.its.jpo.asn.j2735.r2024.BaseSerializeTest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for the SensorDataSharingMessage class.
 */
public class SensorDataSharingMessageTest extends BaseSerializeTest<SensorDataSharingMessage> {
  
  public SensorDataSharingMessageTest() {
    super(SensorDataSharingMessage.class);
  }

  @ParameterizedTest
  @MethodSource("getXmlResources")
  public void canRoundTripXml(String resourcePath) throws IOException {
    String xml = loadResource(resourcePath);
    SensorDataSharingMessage sdsm = fromXml(xml);
    assertThat(sdsm, notNullValue());
    String roundTripXml = toXml(sdsm);
    assertThat(roundTripXml, isIdenticalTo(xml).ignoreComments().ignoreWhitespace());
  }

  @ParameterizedTest
  @MethodSource("getJsonResources")
  public void canRoundTripJson(String resourcePath) throws IOException {
    String json = loadResource(resourcePath);
    SensorDataSharingMessage sdsm = fromJson(json);
    assertThat(sdsm, notNullValue());
    String roundTripJson = toJson(sdsm);
    assertThat(roundTripJson, jsonEquals(json));
  }

  private static Stream<Arguments> getXmlResources() {
    return getResources("/us/dot/its/jpo/asn/j2735/r2024/SensorDataSharingMessage/xml/data");
  }

  private static Stream<Arguments> getJsonResources() {
    return getResources("/us/dot/its/jpo/asn/j2735/r2024/SensorDataSharingMessage/json");
  }

  protected static Stream<Arguments> getResources(String directory) {
    List<String> resources = listAllResourcesInDirectory(directory);
    var streamBuilder = Stream.<Arguments>builder();
    resources.forEach(resource -> streamBuilder.add(Arguments.of(resource)));
    return streamBuilder.build();
  }
}
