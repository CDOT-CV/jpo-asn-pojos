package us.dot.its.jpo.asn.runtime.serialization;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import org.junit.jupiter.api.Test;
import tools.jackson.dataformat.xml.XmlMapper;
import us.dot.its.jpo.asn.runtime.examples.AInteger;
import us.dot.its.jpo.asn.runtime.examples.ASequence;
import us.dot.its.jpo.asn.runtime.examples.AString;
import us.dot.its.jpo.asn.runtime.examples.ExampleParameterizedASequence;
import us.dot.its.jpo.asn.runtime.examples.MessageContainsSequenceOfOpenType;
import us.dot.its.jpo.asn.runtime.examples.SequenceOfOpenType;

public class SequenceOfOpenTypeSerializerTest {

  @Test
  public void itemsUseTheirRootNames() {
    var first = new ASequence();
    first.setAInt(new AInteger(5));
    first.setAStr(new AString("asdf"));

    var secondValue = new ASequence();
    secondValue.setAInt(new AInteger(6));
    secondValue.setAStr(new AString("qwerty"));
    var second = new ExampleParameterizedASequence();
    second.setValue(secondValue);

    var items = new SequenceOfOpenType();
    items.add(first);
    items.add(second);
    var message = new MessageContainsSequenceOfOpenType();
    message.setId(new AInteger(1));
    message.setItems(items);

    String xml = new XmlMapper().writeValueAsString(message);
    assertThat(xml, isIdenticalTo(EXPECTED_XML).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  // Items are named by class name, or by @JsonRootName if present
  static final String EXPECTED_XML = """
      <MessageContainsSequenceOfOpenType>
        <id>1</id>
        <items>
          <ASequence>
            <a-int>5</a-int>
            <a-str>asdf</a-str>
          </ASequence>
          <ExampleParameterized>
            <messageId>1</messageId>
            <value>
              <ASequence>
                <a-int>6</a-int>
                <a-str>qwerty</a-str>
              </ASequence>
            </value>
          </ExampleParameterized>
        </items>
      </MessageContainsSequenceOfOpenType>
      """;
}
