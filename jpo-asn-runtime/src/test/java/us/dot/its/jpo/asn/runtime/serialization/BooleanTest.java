package us.dot.its.jpo.asn.runtime.serialization;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tools.jackson.databind.exc.ValueInstantiationException;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.dataformat.xml.XmlMapper;
import us.dot.its.jpo.asn.runtime.BaseSerializeTest;
import us.dot.its.jpo.asn.runtime.examples.ExampleBoolean;
import us.dot.its.jpo.asn.runtime.examples.MessageContainsBoolean;
import us.dot.its.jpo.asn.runtime.types.Asn1Boolean;

public class BooleanTest extends BaseSerializeTest<MessageContainsBoolean> {

  public BooleanTest() {
    super(MessageContainsBoolean.class);
  }

  @ParameterizedTest
  @MethodSource("xmlValues")
  public void canRoundTripXml(final String description, final String xml, final boolean flag)
      throws IOException {
    MessageContainsBoolean m = fromXml(xml);
    assertThat(description, m.getFlag().getValue(), equalTo(flag));
    assertThat(description, m.getExample(), instanceOf(ExampleBoolean.class));
    assertThat(description, m.getExample().getValue(), equalTo(!flag));
    assertThat(description, toXml(m),
        isIdenticalTo(xml).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @ParameterizedTest
  @MethodSource("jsonValues")
  public void canRoundTripJson(final String description, final String json, final boolean flag)
      throws IOException {
    MessageContainsBoolean m = fromJson(json);
    assertThat(description, m.getFlag().getValue(), equalTo(flag));
    assertThat(description, m.getExample(), instanceOf(ExampleBoolean.class));
    assertThat(description, m.getExample().getValue(), equalTo(!flag));
    assertThat(description, toJson(m), jsonEquals(json));
  }

  @Test
  public void canReadRootXml() {
    var bool = new XmlMapper().readValue("<Asn1Boolean><true/></Asn1Boolean>", Asn1Boolean.class);
    assertThat(bool.getValue(), equalTo(true));
  }

  @Test
  public void canReadRootJsonSubclass() {
    var bool = new JsonMapper().readValue("true", ExampleBoolean.class);
    assertThat(bool, instanceOf(ExampleBoolean.class));
    assertThat(bool.getValue(), equalTo(true));
  }

  @Test
  public void subclassWithoutDefaultConstructor() {
    var mapper = new JsonMapper();
    var ex = assertThrows(ValueInstantiationException.class,
        () -> mapper.readValue("true", NoDefaultConstructorBoolean.class));
    assertThat(ex.getMessage(), containsString("Failed to create instance"));
  }

  public static class NoDefaultConstructorBoolean extends Asn1Boolean {
    public NoDefaultConstructorBoolean(boolean value) {
      super(value);
    }
  }

  private static Stream<Arguments> xmlValues() {
    return Stream.of(
        Arguments.of("true", XML_TRUE, true),
        Arguments.of("false", XML_FALSE, false)
    );
  }

  private static Stream<Arguments> jsonValues() {
    return Stream.of(
        Arguments.of("true", JSON_TRUE, true),
        Arguments.of("false", JSON_FALSE, false)
    );
  }

  static final String XML_TRUE = """
      <MessageContainsBoolean>
        <flag><true/></flag>
        <example><false/></example>
      </MessageContainsBoolean>
      """;

  static final String XML_FALSE = """
      <MessageContainsBoolean>
        <flag><false/></flag>
        <example><true/></example>
      </MessageContainsBoolean>
      """;

  static final String JSON_TRUE = """
      {
        "flag": true,
        "example": false
      }
      """;

  static final String JSON_FALSE = """
      {
        "flag": false,
        "example": true
      }
      """;
}
