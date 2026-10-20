package us.dot.its.jpo.asn.runtime.serialization;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.io.IOException;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.StdSerializer;
import tools.jackson.dataformat.xml.XmlMapper;
import us.dot.its.jpo.asn.runtime.BaseSerializeTest;
import us.dot.its.jpo.asn.runtime.examples.ASequence;
import us.dot.its.jpo.asn.runtime.examples.MessageContainsNestedSequenceOf;
import us.dot.its.jpo.asn.runtime.examples.MessageContainsNestedSequenceOf.SequenceOfSequences;

@Slf4j
public class NestedSequenceOfTest extends BaseSerializeTest<MessageContainsNestedSequenceOf> {

  public NestedSequenceOfTest() {
    super(MessageContainsNestedSequenceOf.class);
  }

  @ParameterizedTest
  @MethodSource("xmlValues")
  public void canRoundTripXml(final String description, final String xml, final int numIntegers,
      final int numSequences) throws IOException {
    MessageContainsNestedSequenceOf m = fromXml(xml);
    assertThat(description, m.getIntegers(), hasSize(numIntegers));
    assertThat(description, m.getSequences(), hasSize(numSequences));
    assertThat(description, m.getIntegers().getFirst().getValue(), equalTo(1L));
    assertThat(description, m.getSequences().getFirst().getAStr().getValue(), equalTo("asdf"));
    assertThat(description, toXml(m),
        isIdenticalTo(xml).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @ParameterizedTest
  @MethodSource("jsonValues")
  public void canRoundTripJson(final String description, final String json, final int numIntegers,
      final int numSequences) throws IOException {
    MessageContainsNestedSequenceOf m = fromJson(json);
    assertThat(description, m.getIntegers(), hasSize(numIntegers));
    assertThat(description, m.getSequences(), hasSize(numSequences));
    assertThat(description, toJson(m), jsonEquals(json));
  }

  @Test
  public void itemSerializationErrorIsReported() {
    var sequences = new SequenceOfSequences();
    sequences.add(new FailingSequence());
    var m = new MessageContainsNestedSequenceOf();
    m.setSequences(sequences);
    var mapper = new XmlMapper();
    var ex = assertThrows(JacksonException.class, () -> mapper.writeValueAsString(m));
    assertThat(ex.getMessage(), containsString("Item serialization failed"));
  }

  @JsonSerialize(using = FailingSerializer.class)
  public static class FailingSequence extends ASequence {
  }

  public static class FailingSerializer extends StdSerializer<FailingSequence> {
    public FailingSerializer() {
      super(FailingSequence.class);
    }

    @Override
    public void serialize(FailingSequence value, JsonGenerator gen, SerializationContext ctxt) {
      throw new IllegalStateException("Item serialization failed");
    }
  }

  private static Stream<Arguments> xmlValues() {
    return Stream.of(
        Arguments.of("Single items", XML_SINGLE, 1, 1),
        Arguments.of("Multiple items", XML_MULTIPLE, 3, 2)
    );
  }

  private static Stream<Arguments> jsonValues() {
    return Stream.of(
        Arguments.of("Single items", JSON_SINGLE, 1, 1),
        Arguments.of("Multiple items", JSON_MULTIPLE, 3, 2)
    );
  }

  static final String XML_SINGLE = """
      <MessageContainsNestedSequenceOf>
        <integers>
          <INTEGER>1</INTEGER>
        </integers>
        <sequences>
          <SEQUENCE>
            <a-int>5</a-int>
            <a-str>asdf</a-str>
          </SEQUENCE>
        </sequences>
      </MessageContainsNestedSequenceOf>
      """;

  static final String XML_MULTIPLE = """
      <MessageContainsNestedSequenceOf>
        <integers>
          <INTEGER>1</INTEGER>
          <INTEGER>2</INTEGER>
          <INTEGER>3</INTEGER>
        </integers>
        <sequences>
          <SEQUENCE>
            <a-int>5</a-int>
            <a-str>asdf</a-str>
          </SEQUENCE>
          <SEQUENCE>
            <a-int>6</a-int>
            <a-str>qwerty</a-str>
          </SEQUENCE>
        </sequences>
      </MessageContainsNestedSequenceOf>
      """;

  static final String JSON_SINGLE = """
      {
        "integers": [1],
        "sequences": [
          {"a-int": 5, "a-str": "asdf"}
        ]
      }
      """;

  static final String JSON_MULTIPLE = """
      {
        "integers": [1, 2, 3],
        "sequences": [
          {"a-int": 5, "a-str": "asdf"},
          {"a-int": 6, "a-str": "qwerty"}
        ]
      }
      """;
}
