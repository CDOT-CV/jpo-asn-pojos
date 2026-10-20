package us.dot.its.jpo.asn.runtime.serialization;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import us.dot.its.jpo.asn.runtime.BaseSerializeTest;
import us.dot.its.jpo.asn.runtime.examples.AInteger;
import us.dot.its.jpo.asn.runtime.examples.ASequence;
import us.dot.its.jpo.asn.runtime.examples.AString;
import us.dot.its.jpo.asn.runtime.examples.MessageContainsWrappedSequenceOf;
import us.dot.its.jpo.asn.runtime.examples.MessageContainsWrappedSequenceOf.SequenceOfAInteger;
import us.dot.its.jpo.asn.runtime.examples.MessageContainsWrappedSequenceOf.SequenceOfASequence;

public class WrappedSequenceOfTest extends BaseSerializeTest<MessageContainsWrappedSequenceOf> {

  public WrappedSequenceOfTest() {
    super(MessageContainsWrappedSequenceOf.class);
  }

  @ParameterizedTest
  @MethodSource("xmlValues")
  public void canRoundTripXml(final String description, final String xml, final int numSequences,
      final int numIntegers) throws IOException {
    MessageContainsWrappedSequenceOf m = fromXml(xml);
    assertThat(description, m.getSequences(), hasSize(numSequences));
    assertThat(description, m.getIntegers(), hasSize(numIntegers));
    assertThat(description, m.getSequences().getFirst().getAStr().getValue(), equalTo("asdf"));
    assertThat(description, m.getIntegers().getFirst().getValue(), equalTo(1L));
    assertThat(description, toXml(m),
        isIdenticalTo(xml).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @ParameterizedTest
  @MethodSource("jsonValues")
  public void canRoundTripJson(final String description, final String json, final int numSequences,
      final int numIntegers) throws IOException {
    MessageContainsWrappedSequenceOf m = fromJson(json);
    assertThat(description, m.getSequences(), hasSize(numSequences));
    assertThat(description, m.getIntegers(), hasSize(numIntegers));
    assertThat(description, toJson(m), jsonEquals(json));
  }

  @Test
  public void canSerializeXml() {
    var sequences = new SequenceOfASequence();
    sequences.add(sequence(5, "asdf"));
    sequences.add(sequence(6, "qwerty"));
    var integers = new SequenceOfAInteger();
    integers.add(new AInteger(1L));
    integers.add(new AInteger(2L));
    integers.add(new AInteger(3L));
    var m = new MessageContainsWrappedSequenceOf();
    m.setSequences(sequences);
    m.setIntegers(integers);
    assertThat(toXml(m),
        isIdenticalTo(XML_MULTIPLE).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @Test
  public void omitsAbsentOptionalXml() throws IOException {
    var sequences = new SequenceOfASequence();
    sequences.add(sequence(5, "asdf"));
    var m = new MessageContainsWrappedSequenceOf();
    m.setSequences(sequences);
    assertThat(toXml(m),
        isIdenticalTo(XML_NO_INTEGERS).ignoreWhitespace().ignoreElementContentWhitespace());
    assertThat(fromXml(XML_NO_INTEGERS).getIntegers(), nullValue());
  }

  private static ASequence sequence(long aInt, String aStr) {
    var sequence = new ASequence();
    sequence.setAInt(new AInteger(aInt));
    sequence.setAStr(new AString(aStr));
    return sequence;
  }

  private static Stream<Arguments> xmlValues() {
    return Stream.of(
        Arguments.of("Single items", XML_SINGLE, 1, 1),
        Arguments.of("Multiple items", XML_MULTIPLE, 2, 3)
    );
  }

  private static Stream<Arguments> jsonValues() {
    return Stream.of(
        Arguments.of("Single items", JSON_SINGLE, 1, 1),
        Arguments.of("Multiple items", JSON_MULTIPLE, 2, 3)
    );
  }

  static final String XML_SINGLE = """
      <MessageContainsWrappedSequenceOf>
        <sequences>
          <A-Sequence>
            <a-int>5</a-int>
            <a-str>asdf</a-str>
          </A-Sequence>
        </sequences>
        <integers>
          <A-Integer>1</A-Integer>
        </integers>
      </MessageContainsWrappedSequenceOf>
      """;

  static final String XML_MULTIPLE = """
      <MessageContainsWrappedSequenceOf>
        <sequences>
          <A-Sequence>
            <a-int>5</a-int>
            <a-str>asdf</a-str>
          </A-Sequence>
          <A-Sequence>
            <a-int>6</a-int>
            <a-str>qwerty</a-str>
          </A-Sequence>
        </sequences>
        <integers>
          <A-Integer>1</A-Integer>
          <A-Integer>2</A-Integer>
          <A-Integer>3</A-Integer>
        </integers>
      </MessageContainsWrappedSequenceOf>
      """;

  static final String XML_NO_INTEGERS = """
      <MessageContainsWrappedSequenceOf>
        <sequences>
          <A-Sequence>
            <a-int>5</a-int>
            <a-str>asdf</a-str>
          </A-Sequence>
        </sequences>
      </MessageContainsWrappedSequenceOf>
      """;

  static final String JSON_SINGLE = """
      {
        "sequences": [
          {"a-int": 5, "a-str": "asdf"}
        ],
        "integers": [1]
      }
      """;

  static final String JSON_MULTIPLE = """
      {
        "sequences": [
          {"a-int": 5, "a-str": "asdf"},
          {"a-int": 6, "a-str": "qwerty"}
        ],
        "integers": [1, 2, 3]
      }
      """;
}
