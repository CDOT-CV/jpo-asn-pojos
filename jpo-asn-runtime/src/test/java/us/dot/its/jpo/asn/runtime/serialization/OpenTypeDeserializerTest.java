package us.dot.its.jpo.asn.runtime.serialization;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import us.dot.its.jpo.asn.runtime.examples.AInteger;
import us.dot.its.jpo.asn.runtime.examples.ASequence;
import us.dot.its.jpo.asn.runtime.examples.AString;
import us.dot.its.jpo.asn.runtime.examples.ExampleWithOpenType;

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
  public void roundTripOpenTypeToXml(String name) throws JsonProcessingException {
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


}
