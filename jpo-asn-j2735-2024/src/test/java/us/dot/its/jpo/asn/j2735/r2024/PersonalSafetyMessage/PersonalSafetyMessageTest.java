package us.dot.its.jpo.asn.j2735.r2024.PersonalSafetyMessage;

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

public class PersonalSafetyMessageTest extends BaseSerializeTest<PersonalSafetyMessage> {

  public PersonalSafetyMessageTest() {
    super(PersonalSafetyMessage.class);
  }

  @ParameterizedTest
  @MethodSource("getXmlResourcesPropulsionTypes")
  public void canRoundTripXml_PropulsionTypesTest(String resourcePath) throws IOException {
    String xml = loadResource(resourcePath);
    PersonalSafetyMessage psm = fromXml(xml);
    assertThat(psm, notNullValue());
    String roundTripXml = toXml(psm);
    assertThat(roundTripXml, isIdenticalTo(xml).ignoreComments().ignoreWhitespace());
  }

  @ParameterizedTest
  @MethodSource("getJsonResourcesPropulsionTypes")
  public void canRoundTripJson_PropulsionTypesTest(String resourcePath) throws IOException {
    String json = loadResource(resourcePath);
    PersonalSafetyMessage psm = fromJson(json);
    assertThat(psm, notNullValue());
    String roundTripJson = toJson(psm);
    assertThat(roundTripJson, jsonEquals(json));
  }

  private static Stream<Arguments> getXmlResourcesPropulsionTypes() {
    return getXmlResources("propulsion_types");
  }

  private static Stream<Arguments> getJsonResourcesPropulsionTypes() {
    return getJsonResources("propulsion_types");
  }

  private static Stream<Arguments> getXmlResources(String subdirectory)  {
    return getResources("/us/dot/its/jpo/asn/j2735/r2024/PersonalSafetyMessage/xml/" + subdirectory);
  }

  private static Stream<Arguments> getJsonResources(String subdirectory) {
    return getResources("/us/dot/its/jpo/asn/j2735/r2024/PersonalSafetyMessage/json/" + subdirectory);
  }

  protected static Stream<Arguments> getResources(String directory) {
    List<String> resources = listAllResourcesInDirectory(directory);
    var streamBuilder = Stream.<Arguments>builder();
    resources.forEach(resource -> streamBuilder.add(Arguments.of(resource)));
    return streamBuilder.build();
  }
}
