package us.dot.its.jpo.asn.runtime.serialization;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.exc.MismatchedInputException;
import us.dot.its.jpo.asn.runtime.examples.FruitEnum;
import tools.jackson.dataformat.xml.XmlMapper;
import tools.jackson.dataformat.xml.XmlReadFeature;
import us.dot.its.jpo.asn.runtime.BaseSerializeTest;
import us.dot.its.jpo.asn.runtime.examples.MessageContainsSequenceOfEnumerated;
import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SequenceOfEnumeratedDeserializerTest extends
    BaseSerializeTest<MessageContainsSequenceOfEnumerated> {

  public SequenceOfEnumeratedDeserializerTest() {
    super(MessageContainsSequenceOfEnumerated.class);
  }

  @Test
  public void unknownEnumValue() {
    var ex = assertThrows(MismatchedInputException.class, () -> fromXml(XML_UNKNOWN));
    assertThat(ex.getMessage(),
        containsString("Invalid enum value: kiwi. Must be one of: apple, orange, banana"));
  }

  @Test
  public void enumElementWithContent() throws IOException {
    MessageContainsSequenceOfEnumerated m = fromXml(XML_WITH_CONTENT);
    assertThat(m.getFruits(), contains(FruitEnum.APPLE, FruitEnum.ORANGE));
    assertThat(m.getId().getValue(), equalTo(2L));
  }

  @Test
  public void emptySequenceOf() throws IOException {
    MessageContainsSequenceOfEnumerated m = fromXml(XML_EMPTY);
    assertThat(m.getFruits(), empty());
    assertThat(m.getId().getValue(), equalTo(1L));
  }

  @ParameterizedTest
  @MethodSource("xmlValues")
  public void canRoundTripXml(final String description, final String xml) throws IOException {
    MessageContainsSequenceOfEnumerated m = fromXml(xml);
    assertThat(description, m, notNullValue());
    String roundTripXml = toXml(m);
    assertThat(description, roundTripXml, isIdenticalTo(xml).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @ParameterizedTest
  @MethodSource("xmlValues")
  public void canRoundTripXmlWithEmptyElementAsNull(final String description, final String xml)
      throws IOException {
    var mapper = XmlMapper.builder()
        .enable(XmlReadFeature.EMPTY_ELEMENT_AS_NULL)
        .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
        .build();
    MessageContainsSequenceOfEnumerated m =
        mapper.readValue(xml, MessageContainsSequenceOfEnumerated.class);
    assertThat(description, m, notNullValue());
    String roundTripXml = toXml(m);
    assertThat(description, roundTripXml, isIdenticalTo(xml).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @ParameterizedTest
  @MethodSource("jsonValues")
  public void canRoundTripJson(final String description, final String json) throws IOException {
    MessageContainsSequenceOfEnumerated m = fromJson(json);
    assertThat(description, m, notNullValue());
    String roundTripJson = toJson(m);
    assertThat(description, roundTripJson, jsonEquals(json));
  }

  private static Stream<Arguments> xmlValues() {
    return Stream.of(
        Arguments.of("Single value", XML_SINGLE),
        Arguments.of("Unique values", XML_UNIQUE),
        Arguments.of("Duplicate values, ordered", XML_DUPLICATES),
        Arguments.of("Duplicate values, mixed order", XML_MIXED)
    );
  }

  private static Stream<Arguments> jsonValues() {
    return Stream.of(
        Arguments.of("Single value", JSON_SINGLE),
        Arguments.of("Unique values", JSON_UNIQUE),
        Arguments.of("Duplicate values, ordered", JSON_DUPLICATES),
        Arguments.of("Duplicate values, mixed order", JSON_MIXED)
    );
  }

  public static final String XML_UNKNOWN = """
      <MessageContainsSequenceOfEnumerated>
        <id>1</id>
        <fruits>
          <apple/>
          <kiwi/>
        </fruits>
      </MessageContainsSequenceOfEnumerated>
      """;

  public static final String XML_WITH_CONTENT = """
      <MessageContainsSequenceOfEnumerated>
        <fruits>
          <apple>
            <unexpected>1</unexpected>
          </apple>
          <orange/>
        </fruits>
        <id>2</id>
      </MessageContainsSequenceOfEnumerated>
      """;

  public static final String XML_EMPTY = """
      <MessageContainsSequenceOfEnumerated>
        <fruits/>
        <id>1</id>
      </MessageContainsSequenceOfEnumerated>
      """;

  public static final String XML_SINGLE = """
      <MessageContainsSequenceOfEnumerated>
        <id>1</id>
        <fruits>
          <apple/>
        </fruits>
      </MessageContainsSequenceOfEnumerated>
      """;

  public static final String JSON_SINGLE = """
      {
        "id": 1,
        "fruits": [
          "apple"
        ]
      }
      """;

  public static final String XML_UNIQUE = """
      <MessageContainsSequenceOfEnumerated>
        <id>1</id>
        <fruits>
          <apple/>
          <orange/>
          <banana/>
        </fruits>
      </MessageContainsSequenceOfEnumerated>
      """;

  public static final String JSON_UNIQUE = """
      {
        "id": 1,
        "fruits": [
          "apple",
          "orange",
          "banana"
        ]
      }
      """;

  public static final String XML_DUPLICATES = """
      <MessageContainsSequenceOfEnumerated>
        <id>1</id>
        <fruits>
          <apple/>
          <apple/>
          <orange/>
          <orange/>
          <banana/>
          <banana/>
        </fruits>
      </MessageContainsSequenceOfEnumerated>
      """;

  public static final String JSON_DUPLICATES = """
      {
        "id": 1,
        "fruits": [
          "apple",
          "apple",
          "orange",
          "orange",
          "banana",
          "banana"
        ]
      }
      """;

  public static final String XML_MIXED = """
      <MessageContainsSequenceOfEnumerated>
        <id>1</id>
        <fruits>
          <apple/>
          <orange/>
          <apple/>
          <banana/>
          <orange/>
          <banana/>
        </fruits>
      </MessageContainsSequenceOfEnumerated>
      """;

  public static final String JSON_MIXED = """
    {
        "id": 1,
        "fruits": [
          "apple",
          "orange",
          "apple",
          "banana",
          "orange",
          "banana"
        ]
      }
    """;
}


