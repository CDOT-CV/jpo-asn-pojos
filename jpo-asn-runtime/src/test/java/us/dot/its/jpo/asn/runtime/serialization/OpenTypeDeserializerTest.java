package us.dot.its.jpo.asn.runtime.serialization;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.stream.Stream;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.exc.MismatchedInputException;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import us.dot.its.jpo.asn.runtime.examples.AInteger;
import us.dot.its.jpo.asn.runtime.examples.ASequence;
import us.dot.its.jpo.asn.runtime.examples.AString;
import us.dot.its.jpo.asn.runtime.examples.ExampleWithOpenType;
import us.dot.its.jpo.asn.runtime.examples.ExampleWithUnwrappedOpenType;

@Slf4j
public class OpenTypeDeserializerTest {

  @ParameterizedTest
  @CsvSource({
      "namewithnospaces",
      "name with spaces",
      " name with padding ",
      "name & with & ampersands",
      "name with < angle > brackets"
  })
  public void roundTripOpenTypeToXml(String name) throws JacksonException {
    var mapper = new XmlMapper();
    var example = new ExampleWithOpenType();
    example.setMessageId(new AInteger(10));
    var value = new ASequence();
    value.setAInt(new AInteger(20));
    value.setAStr(new AString(name));
    example.setValue(value);
    String xml = mapper.writeValueAsString(example);
    log.debug(xml);
    var roundTrip = mapper.readValue(xml, ExampleWithOpenType.class);
    String roundTripXml = mapper.writeValueAsString(roundTrip);
    log.debug(roundTripXml);
    assertThat(roundTripXml, isIdenticalTo(xml).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @Test
  public void openTypeFollowedBySibling() throws JacksonException {
    var mapper = new XmlMapper();
    var example = mapper.readValue(XML_OPEN_TYPE_FIRST, ExampleWithOpenType.class);
    assertThat(example.getMessageId().getValue(), equalTo(10L));
    assertThat(example.getValue().getAInt().getValue(), equalTo(20L));
    assertThat(example.getValue().getAStr().getValue(), equalTo("asdf"));
  }

  @Test
  public void canRoundTripUnwrappedOpenTypeXml() {
    var mapper = new XmlMapper();
    var example = mapper.readValue(XML_UNWRAPPED_OPEN_TYPE, ExampleWithUnwrappedOpenType.class);
    assertThat(example.getValue(), hasSize(2));
    assertThat(example.getValue().getLast().getAStr().getValue(), equalTo("qwerty"));
    assertThat(mapper.writeValueAsString(example),
        isIdenticalTo(XML_UNWRAPPED_OPEN_TYPE).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @Test
  public void canRoundTripOpenTypeJson() {
    var mapper = new JsonMapper();
    var example = mapper.readValue(JSON_OPEN_TYPE, ExampleWithOpenType.class);
    assertThat(example.getValue().getAStr().getValue(), equalTo("asdf"));
    assertThat(mapper.writeValueAsString(example), jsonEquals(JSON_OPEN_TYPE));
  }

  @ParameterizedTest
  @MethodSource("invalidXmlValues")
  public void invalidOpenTypeXml(final String description, final String xml,
      final String expectedMessage) {
    var mapper = new XmlMapper();
    var ex = assertThrows(MismatchedInputException.class,
        () -> mapper.readValue(xml, ExampleWithOpenType.class));
    assertThat(description, ex.getMessage(), containsString(expectedMessage));
  }

  private static Stream<Arguments> invalidXmlValues() {
    return Stream.of(
        Arguments.of("Empty open type", XML_EMPTY_OPEN_TYPE, "Expected an open type element"),
        Arguments.of("Two elements", XML_TWO_ELEMENTS, "Expected exactly one open type element")
    );
  }

  static final String XML_UNWRAPPED_OPEN_TYPE = """
      <MessageFrame>
        <messageId>10</messageId>
        <value>
          <SequenceOfASequence>
            <ASequence>
              <a-int>20</a-int>
              <a-str>asdf</a-str>
            </ASequence>
            <ASequence>
              <a-int>21</a-int>
              <a-str>qwerty</a-str>
            </ASequence>
          </SequenceOfASequence>
        </value>
      </MessageFrame>
      """;

  static final String JSON_OPEN_TYPE = """
      {
        "messageId": 10,
        "value": {
          "ASequence": {"a-int": 20, "a-str": "asdf"}
        }
      }
      """;

  static final String XML_EMPTY_OPEN_TYPE = """
      <MessageFrame>
        <messageId>10</messageId>
        <value/>
      </MessageFrame>
      """;

  static final String XML_TWO_ELEMENTS = """
      <MessageFrame>
        <messageId>10</messageId>
        <value>
          <ASequence>
            <a-int>20</a-int>
            <a-str>asdf</a-str>
          </ASequence>
          <ASequence>
            <a-int>21</a-int>
            <a-str>qwerty</a-str>
          </ASequence>
        </value>
      </MessageFrame>
      """;

  static final String XML_OPEN_TYPE_FIRST = """
      <MessageFrame>
        <value>
          <ASequence>
            <a-int>20</a-int>
            <a-str>asdf</a-str>
          </ASequence>
        </value>
        <messageId>10</messageId>
      </MessageFrame>
      """;

}
