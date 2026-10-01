package us.dot.its.jpo.asn.runtime.serialization;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import tools.jackson.core.JacksonException;
import us.dot.its.jpo.asn.runtime.BaseSerializeTest;
import us.dot.its.jpo.asn.runtime.examples.MessageContainsSequenceOfChoice;
import java.io.IOException;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Slf4j
public class SequenceOfChoiceDeserializerTest extends BaseSerializeTest<MessageContainsSequenceOfChoice> {

  public SequenceOfChoiceDeserializerTest() {
    super(MessageContainsSequenceOfChoice.class);
  }

  @ParameterizedTest
  @MethodSource("xmlValues")
  public void canRoundTripXml(final String description, final String xml) throws IOException {
    MessageContainsSequenceOfChoice m = fromXml(xml);
    assertThat(description, m, notNullValue());
    log.debug("{}", m);
    String roundTripXml = toXml(m);
    assertThat(description, roundTripXml, isIdenticalTo(xml).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @ParameterizedTest
  @MethodSource("jsonValues")
  public void canRoundTripJson(final String description, final String json) throws IOException {
    MessageContainsSequenceOfChoice m = fromJson(json);
    assertThat(description, m, notNullValue());
    String roundTripJson = toJson(m);
    assertThat(description, roundTripJson, jsonEquals(json));
  }

  @Test
  public void emptySequenceOfDoesNotConsumeSiblings() throws IOException {
    MessageContainsSequenceOfChoice m = fromXml(XML_EMPTY);
    assertThat(m.getId().getValue(), equalTo(10L));
    assertThat(m.getNum().getValue(), equalTo(7L));
    assertThat(m.getChoices(), anyOf(nullValue(), empty()));
  }

  @ParameterizedTest
  @MethodSource("malformedXmlValues")
  public void malformedXmlDoesNotHang(final String description, final String xml)  {
    JacksonException jpe = assertThrows(
        JacksonException.class,
        () -> fromXml(xml),
        "Invalid xml: Expect JsonProcessingException and not stack overflow or anything else"
    );
    log.debug("Malformed xml threw expected exception: {}", jpe.getMessage());
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

  private static Stream<Arguments> malformedXmlValues() {
    return Stream.of(
        Arguments.of("Outer element not closed", XML_MALFORMED1),
        Arguments.of("Choice element not closed", XML_MALFORMED2),
        Arguments.of("random junk", XML_MALFORMED3)
    );
  }

  public static final String XML_SINGLE = """
      <MessageContainsSequenceOfChoice>
        <id>10</id>
        <choices>
          <a>
            <a-int>5</a-int>
            <a-str>asdf</a-str>
          </a>
        </choices>
        <num>7</num>
      </MessageContainsSequenceOfChoice>
      """;

  public static final String JSON_SINGLE = """
      {
        "id": 10,
        "choices": [
          {
            "a": {"a-int": 5, "a-str": "asdf"}
          }
        ],
        "num": 7
      }
      """;

  public static final String XML_UNIQUE = """
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
        </choices>
        <num>7</num>
      </MessageContainsSequenceOfChoice>
      """;

  public static final String JSON_UNIQUE = """
      {
        "id": 10,
        "choices": [
          {
            "a": {"a-int": 5, "a-str": "asdf"}
          },
          {
            "b": {"b-int": 6, "b-str": "qwerty"}
          }
        ],
        "num": 7
      }
      """;

  public static final String XML_DUPLICATES = """
      <MessageContainsSequenceOfChoice>
        <id>10</id>
        <choices>
          <a>
            <a-int>5</a-int>
            <a-str>asdf</a-str>
          </a>
          <a>
            <a-int>10</a-int>
            <a-str>yuio</a-str>
          </a>
          <b>
            <b-int>6</b-int>
            <b-str>qwerty</b-str>
          </b>
          <b>
            <b-int>12</b-int>
            <b-str>hjkl</b-str>
          </b>
        </choices>
        <num>7</num>
      </MessageContainsSequenceOfChoice>
      """;

  public static final String JSON_DUPLICATES = """
      {
        "id": 10,
        "choices": [
          {
            "a": {"a-int": 5, "a-str": "asdf"}
          },
          {
            "a": {"a-int": 10, "a-str": "yuio"}
          },
          {
            "b": {"b-int": 6, "b-str": "qwerty"}
          },
          {
            "b": {"b-int": 12, "b-str": "hjkl"}
          }
        ],
        "num": 7
      }
      """;

  public static final String XML_MIXED = """
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
      """;

  public static final String JSON_MIXED = """
      {
        "id": 10,
        "choices": [
          {
            "a": {"a-int": 5, "a-str": "asdf"}
          },
          {
            "b": {"b-int": 6, "b-str": "qwerty"}
          },
          {
            "a": {"a-int": 10, "a-str": "yuio"}
          },
          {
            "b": {"b-int": 12, "b-str": "hjkl"}
          }
        ],
        "num": 7
      }
      """;

  public static final String XML_EMPTY = """
      <MessageContainsSequenceOfChoice>
        <id>10</id>
        <choices/>
        <num>7</num>
      </MessageContainsSequenceOfChoice>
      """;

  public static final String XML_MALFORMED1 = """
      <MessageContainsSequenceOfChoice>
        <id>10</id>
        <choices>
          <a>
            <a-int>5</a-int>
            <a-str>asdf</a-str>
          </a>
        </choices>
        <num>7</num>
      """;

  public static final String XML_MALFORMED2 = """
      <MessageContainsSequenceOfChoice>
        <id>10</id>
        <choices>
          <a>
            <a-int>5</a-int>
            <a-str>asdf</a-str>
          </a>
      """;

  public static final String XML_MALFORMED3 = """
      <asdf><<< /
      """;
}
