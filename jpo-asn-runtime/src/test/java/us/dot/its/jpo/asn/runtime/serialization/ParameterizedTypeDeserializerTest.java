package us.dot.its.jpo.asn.runtime.serialization;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.annotation.JsonDeserialize;
import us.dot.its.jpo.asn.runtime.annotations.Asn1ParameterizedTypes;
import us.dot.its.jpo.asn.runtime.annotations.Asn1ParameterizedTypes.IdType;
import us.dot.its.jpo.asn.runtime.examples.AInteger;
import us.dot.its.jpo.asn.runtime.examples.AString;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.exc.MismatchedInputException;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.dataformat.xml.XmlMapper;
import us.dot.its.jpo.asn.runtime.examples.ExampleParameterized;
import us.dot.its.jpo.asn.runtime.examples.ExampleParameterizedASequence;
import us.dot.its.jpo.asn.runtime.examples.ExampleParameterizedContainer;
import us.dot.its.jpo.asn.runtime.examples.ExampleParameterizedSequenceOfChoice;

public class ParameterizedTypeDeserializerTest {

  private static final XmlMapper XML_MAPPER = XmlMapper.builder()
      .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY).build();
  private static final ObjectMapper JSON_MAPPER = JsonMapper.builder()
      .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY).build();

  @ParameterizedTest
  @MethodSource("xmlValues")
  public void canRoundTripXml(final String description, final String xml,
      final Class<?> expectedType) {
    var frame = XML_MAPPER.readValue(xml, ExampleParameterized.class);
    assertThat(description, frame, instanceOf(expectedType));
    String roundTripXml = XML_MAPPER.writeValueAsString(frame);
    assertThat(description, roundTripXml,
        isIdenticalTo(xml).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @ParameterizedTest
  @MethodSource("jsonValues")
  public void canRoundTripJson(final String description, final String json,
      final Class<?> expectedType) {
    var frame = JSON_MAPPER.readValue(json, ExampleParameterized.class);
    assertThat(description, frame, instanceOf(expectedType));
    String roundTripJson = JSON_MAPPER.writeValueAsString(frame);
    assertThat(description, roundTripJson, jsonEquals(json));
  }

  @Test
  public void canRoundTripXmlNestedInSequence() {
    var container = XML_MAPPER.readValue(XML_CONTAINER, ExampleParameterizedContainer.class);
    assertThat(container.getFrame(), instanceOf(ExampleParameterizedASequence.class));
    assertThat(container.getAfter().getValue(), equalTo(4L));
    String roundTripXml = XML_MAPPER.writeValueAsString(container);
    assertThat(roundTripXml,
        isIdenticalTo(XML_CONTAINER).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @Test
  public void canRoundTripStringIdXml() {
    var frame = XML_MAPPER.readValue(XML_STRING_ID, StringIdParameterized.class);
    assertThat(frame, instanceOf(StringIdFive.class));
    assertThat(frame.getValue().getValue(), equalTo(5L));
    assertThat(XML_MAPPER.writeValueAsString(frame),
        isIdenticalTo(XML_STRING_ID).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @Test
  public void canRoundTripStringIdJson() {
    var frame = JSON_MAPPER.readValue(JSON_STRING_ID, StringIdParameterized.class);
    assertThat(frame, instanceOf(StringIdFive.class));
    assertThat(JSON_MAPPER.writeValueAsString(frame), jsonEquals(JSON_STRING_ID));
  }

  @ParameterizedTest
  @MethodSource("invalidValues")
  public void invalidValue(final String description, final ObjectMapper mapper, final String value,
      final Class<?> type, final String expectedMessage) {
    var ex = assertThrows(RuntimeException.class, () -> mapper.readValue(value, type));
    assertThat(description, ex.getMessage(), containsString(expectedMessage));
  }

  private static Stream<Arguments> invalidValues() {
    return Stream.of(
        Arguments.of("XML unknown id", XML_MAPPER, XML_UNKNOWN_ID, ExampleParameterized.class,
            "Id 9 not found"),
        Arguments.of("JSON unknown id", JSON_MAPPER, JSON_UNKNOWN_ID, ExampleParameterized.class,
            "Id 9 not found"),
        Arguments.of("JSON not an object", JSON_MAPPER, "[1, 2]", ExampleParameterized.class,
            "Not instance of object"),
        Arguments.of("JSON missing id", JSON_MAPPER, JSON_MISSING_ID, ExampleParameterized.class,
            "idPropNode is null"),
        Arguments.of("Missing annotation", JSON_MAPPER, "{}", NoAnnotation.class,
            "Missing Asn1ParameterizedTypes annotation"),
        Arguments.of("No types", JSON_MAPPER, "{}", NoTypes.class,
            "No Types are defined")
    );
  }

  @JsonPropertyOrder({"kind", "value"})
  @Asn1ParameterizedTypes(idProperty = "kind", idType = IdType.STRING, valueProperty = "value",
      value = {@Asn1ParameterizedTypes.Type(value = StringIdFive.class, stringId = "five")})
  @JsonDeserialize(using = StringIdDeserializer.class)
  public abstract static class StringIdParameterized extends Asn1Sequence {
    @JsonProperty("kind")
    @Getter
    @Setter
    protected AString kind;

    @JsonProperty("value")
    @Getter
    @Setter
    protected AInteger value;
  }

  @JsonRootName("StringIdParameterized")
  @JsonDeserialize(using = ValueDeserializer.None.class)
  public static class StringIdFive extends StringIdParameterized {
    public StringIdFive() {
      kind = new AString("five");
    }
  }

  public static class StringIdDeserializer
      extends ParameterizedTypeDeserializer<StringIdParameterized> {
    public StringIdDeserializer() {
      super(StringIdParameterized.class);
    }
  }

  @JsonDeserialize(using = NoAnnotationDeserializer.class)
  public abstract static class NoAnnotation extends Asn1Sequence {
  }

  public static class NoAnnotationDeserializer extends ParameterizedTypeDeserializer<NoAnnotation> {
    public NoAnnotationDeserializer() {
      super(NoAnnotation.class);
    }
  }

  @Asn1ParameterizedTypes(idProperty = "kind", idType = IdType.STRING, valueProperty = "value",
      value = {})
  @JsonDeserialize(using = NoTypesDeserializer.class)
  public abstract static class NoTypes extends Asn1Sequence {
  }

  public static class NoTypesDeserializer extends ParameterizedTypeDeserializer<NoTypes> {
    public NoTypesDeserializer() {
      super(NoTypes.class);
    }
  }

  @Test
  public void xmlIdMustBeFirst() {
    assertThrows(MismatchedInputException.class,
        () -> XML_MAPPER.readValue(XML_ID_NOT_FIRST, ExampleParameterized.class));
  }

  private static Stream<Arguments> xmlValues() {
    return Stream.of(
        Arguments.of("SEQUENCE value", XML_ASEQUENCE, ExampleParameterizedASequence.class),
        Arguments.of("SEQUENCE OF CHOICE value, mixed order", XML_SEQUENCE_OF_CHOICE,
            ExampleParameterizedSequenceOfChoice.class)
    );
  }

  private static Stream<Arguments> jsonValues() {
    return Stream.of(
        Arguments.of("SEQUENCE value", JSON_ASEQUENCE, ExampleParameterizedASequence.class),
        Arguments.of("SEQUENCE OF CHOICE value, mixed order", JSON_SEQUENCE_OF_CHOICE,
            ExampleParameterizedSequenceOfChoice.class)
    );
  }

  static final String XML_ASEQUENCE = """
      <ExampleParameterized>
        <messageId>1</messageId>
        <value>
          <ASequence>
            <a-int>5</a-int>
            <a-str>name &amp; &lt;angle&gt; brackets</a-str>
          </ASequence>
        </value>
      </ExampleParameterized>
      """;

  static final String JSON_ASEQUENCE = """
      {
        "messageId": 1,
        "value": {
          "ASequence": {"a-int": 5, "a-str": "name & <angle> brackets"}
        }
      }
      """;

  // Repeated <a> and <b> elements in mixed order are lost when read into a JsonNode tree
  static final String XML_SEQUENCE_OF_CHOICE = """
      <ExampleParameterized>
        <messageId>2</messageId>
        <value>
          <MessageContainsSequenceOfChoice>
            <id>10</id>
            <choices>
              <a>
                <a-int>5</a-int>
                <a-str>asdf</a-str>
              </a>
              <b>
                <b-int>6</b-int>
                <b-str>qwerty</b-str>
              </b>
              <a>
                <a-int>10</a-int>
                <a-str>yuio</a-str>
              </a>
              <b>
                <b-int>12</b-int>
                <b-str>hjkl</b-str>
              </b>
            </choices>
            <num>7</num>
          </MessageContainsSequenceOfChoice>
        </value>
      </ExampleParameterized>
      """;

  static final String JSON_SEQUENCE_OF_CHOICE = """
      {
        "messageId": 2,
        "value": {
          "MessageContainsSequenceOfChoice": {
            "id": 10,
            "choices": [
              {"a": {"a-int": 5, "a-str": "asdf"}},
              {"b": {"b-int": 6, "b-str": "qwerty"}},
              {"a": {"a-int": 10, "a-str": "yuio"}},
              {"b": {"b-int": 12, "b-str": "hjkl"}}
            ],
            "num": 7
          }
        }
      }
      """;

  static final String XML_STRING_ID = """
      <StringIdParameterized>
        <kind>five</kind>
        <value>5</value>
      </StringIdParameterized>
      """;

  static final String JSON_STRING_ID = """
      {
        "kind": "five",
        "value": 5
      }
      """;

  static final String XML_UNKNOWN_ID = """
      <ExampleParameterized>
        <messageId>9</messageId>
        <value/>
      </ExampleParameterized>
      """;

  static final String JSON_UNKNOWN_ID = """
      {
        "messageId": 9,
        "value": {}
      }
      """;

  static final String JSON_MISSING_ID = """
      {
        "value": {}
      }
      """;

  static final String XML_ID_NOT_FIRST = """
      <ExampleParameterized>
        <value>
          <ASequence>
            <a-int>5</a-int>
            <a-str>asdf</a-str>
          </ASequence>
        </value>
        <messageId>1</messageId>
      </ExampleParameterized>
      """;

  static final String XML_CONTAINER = """
      <ExampleParameterizedContainer>
        <before>3</before>
        <frame>
          <messageId>1</messageId>
          <value>
            <ASequence>
              <a-int>5</a-int>
              <a-str>asdf</a-str>
            </ASequence>
          </value>
        </frame>
        <after>4</after>
      </ExampleParameterizedContainer>
      """;
}
