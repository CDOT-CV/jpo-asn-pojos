package us.dot.its.jpo.asn.runtime.serialization;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.dataformat.xml.XmlMapper;
import us.dot.its.jpo.asn.runtime.examples.AInteger;
import us.dot.its.jpo.asn.runtime.examples.ASequence;
import us.dot.its.jpo.asn.runtime.examples.AString;
import us.dot.its.jpo.asn.runtime.examples.MessageContainsRootSequenceOf;
import us.dot.its.jpo.asn.runtime.examples.MessageContainsRootSequenceOf.FirstList;
import us.dot.its.jpo.asn.runtime.examples.MessageContainsRootSequenceOf.SecondList;

@Slf4j
public class RootSequenceOfSerializerTest {

  @Test
  public void canSerializeRootXml() {
    String xml = new XmlMapper().writeValueAsString(firstList());
    log.debug(xml);
    assertThat(xml, isIdenticalTo(XML_ROOT).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @Test
  public void canSerializePropertiesXml() {
    String xml = new XmlMapper().writeValueAsString(message());
    log.debug(xml);
    assertThat(xml, isIdenticalTo(XML_PROPERTIES).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @Test
  public void canSerializePropertiesJson() {
    String json = new JsonMapper().writeValueAsString(message());
    assertThat(json, jsonEquals(JSON_PROPERTIES));
  }

  private static MessageContainsRootSequenceOf message() {
    var second = new SecondList();
    second.add(sequence(7, "zxcv"));
    var message = new MessageContainsRootSequenceOf();
    message.setFirst(firstList());
    message.setSecond(second);
    return message;
  }

  private static FirstList firstList() {
    var first = new FirstList();
    first.add(sequence(5, "asdf"));
    first.add(sequence(6, "qwerty"));
    return first;
  }

  private static ASequence sequence(long aInt, String aStr) {
    var sequence = new ASequence();
    sequence.setAInt(new AInteger(aInt));
    sequence.setAStr(new AString(aStr));
    return sequence;
  }

  static final String XML_ROOT = """
      <first>
        <ASequence>
          <a-int>5</a-int>
          <a-str>asdf</a-str>
        </ASequence>
        <ASequence>
          <a-int>6</a-int>
          <a-str>qwerty</a-str>
        </ASequence>
      </first>
      """;

  static final String XML_PROPERTIES = """
      <MessageContainsRootSequenceOf>
        <first>
          <ASequence>
            <a-int>5</a-int>
            <a-str>asdf</a-str>
          </ASequence>
          <ASequence>
            <a-int>6</a-int>
            <a-str>qwerty</a-str>
          </ASequence>
        </first>
        <second>
          <ASequence>
            <a-int>7</a-int>
            <a-str>zxcv</a-str>
          </ASequence>
        </second>
      </MessageContainsRootSequenceOf>
      """;

  static final String JSON_PROPERTIES = """
      {
        "first": [
          {"a-int": 5, "a-str": "asdf"},
          {"a-int": 6, "a-str": "qwerty"}
        ],
        "second": [
          {"a-int": 7, "a-str": "zxcv"}
        ]
      }
      """;
}
