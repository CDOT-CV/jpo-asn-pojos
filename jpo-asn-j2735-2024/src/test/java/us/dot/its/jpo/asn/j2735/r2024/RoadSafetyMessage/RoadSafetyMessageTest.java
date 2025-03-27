package us.dot.its.jpo.asn.j2735.r2024.RoadSafetyMessage;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

import us.dot.its.jpo.asn.j2735.r2024.BaseSerializeTest;
import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.ElementSelectors;

public class RoadSafetyMessageTest extends BaseSerializeTest<RoadSafetyMessage> {

  public RoadSafetyMessageTest() {
    super(RoadSafetyMessage.class);
  }

  @ParameterizedTest
  @MethodSource("getXmlResourcesRoot")
  public void canRoundTripXml_LenientTest(String resourcePath) throws IOException {
    String xml = loadResource(resourcePath);
    RoadSafetyMessage rsm = fromXml(xml);
    assertThat(rsm, notNullValue());
    String roundTripXml = toXml(rsm);

    assertThat("Lenient test: Ignore order of sequence-of mixed types",
        roundTripXml,
        isSimilarTo(xml).ignoreWhitespace().normalizeWhitespace()
            .withNodeMatcher(new DefaultNodeMatcher(
                ElementSelectors.byNameAndText
            )));
  }

  @ParameterizedTest
  @MethodSource("getXmlResourcesRoot")
  public void canRoundTripXml_StrictTest(String resourcePath) throws IOException {
    String xml = loadResource(resourcePath);
    RoadSafetyMessage rsm = fromXml(xml);
    assertThat(rsm, notNullValue());
    String roundTripXml = toXml(rsm);

    assertThat("strict test: exact element order", roundTripXml,
        isIdenticalTo(xml).ignoreComments().ignoreWhitespace());
  }

  @ParameterizedTest
  @MethodSource("getJsonResourcesRoot")
  public void canRoundTripJson_Test(String resourcePath) throws IOException {
    String json = loadResource(resourcePath);
    RoadSafetyMessage rsm = fromJson(json);
    assertThat(rsm, notNullValue());
    String roundTripJson = toJson(rsm);

    assertThat("strict test: exact element order", roundTripJson,
        jsonEquals(json));
  }

  private static Stream<Arguments> getXmlResourcesRoot() {
    return getXmlResources("data");
  }

  private static Stream<Arguments> getJsonResourcesRoot() {
    return getJsonResources("data");
  }

  private static Stream<Arguments> getXmlResources(String subdirectory) {
    return getResources("/us/dot/its/jpo/asn/j2735/r2024/RoadSafetyMessage/xml/" + subdirectory);
  }

  private static Stream<Arguments> getJsonResources(String subdirectory) {
    return getResources("/us/dot/its/jpo/asn/j2735/r2024/RoadSafetyMessage/json/" + subdirectory);
  }

}
