package us.dot.its.jpo.asn.runtime.serialization;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
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
