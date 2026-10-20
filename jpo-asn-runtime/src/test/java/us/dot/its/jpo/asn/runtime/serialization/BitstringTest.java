package us.dot.its.jpo.asn.runtime.serialization;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.exc.MismatchedInputException;
import tools.jackson.databind.exc.ValueInstantiationException;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.dataformat.xml.XmlMapper;
import us.dot.its.jpo.asn.runtime.examples.ExampleBitstringFewNamedBits;
import us.dot.its.jpo.asn.runtime.examples.ExampleBitstringVariableSize;
import us.dot.its.jpo.asn.runtime.examples.MessageContainsBitstring;
import us.dot.its.jpo.asn.runtime.types.Asn1Bitstring;

public class BitstringTest {

  @Test
  public void canRoundTripVariableSizeJson() {
    var mapper = new JsonMapper();
    var bitstring = mapper.readValue(JSON_VARIABLE_SIZE, ExampleBitstringVariableSize.class);
    assertThat(bitstring.binaryString(), equalTo("00101"));
    assertThat(mapper.writeValueAsString(bitstring), jsonEquals(JSON_VARIABLE_SIZE));
  }

  @Test
  public void canRoundTripVariableSizeXml() {
    var mapper = new XmlMapper();
    var message = mapper.readValue(XML_VARIABLE_SIZE, MessageContainsBitstring.class);
    assertThat(message.getBits().binaryString(), equalTo("00101"));
    assertThat(mapper.writeValueAsString(message),
        isIdenticalTo(XML_VARIABLE_SIZE).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @Test
  public void variableSizeJsonMissingLength() {
    var mapper = new JsonMapper();
    var ex = assertThrows(MismatchedInputException.class,
        () -> mapper.readValue(JSON_MISSING_LENGTH, ExampleBitstringVariableSize.class));
    assertThat(ex.getMessage(), containsString("missing 'value' or 'length'"));
  }

  @Test
  public void abstractBitstring() {
    var mapper = new JsonMapper();
    var ex = assertThrows(ValueInstantiationException.class,
        () -> mapper.readValue("\"28\"", Asn1Bitstring.class));
    assertThat(ex.getMessage(), containsString("Cannot instantiate abstract class"));
  }

  @Test
  public void bitstringWithoutDefaultConstructor() {
    var mapper = new JsonMapper();
    var ex = assertThrows(ValueInstantiationException.class,
        () -> mapper.readValue("\"28\"", NoDefaultConstructorBitstring.class));
    assertThat(ex.getMessage(), containsString("Failed to create instance"));
  }

  @Test
  public void humanReadableUnknownName() {
    var mapper = new OdeCustomJsonMapper(true);
    var ex = assertThrows(IllegalArgumentException.class,
        () -> mapper.readValue("{\"bogus\": true}", ExampleBitstringFewNamedBits.class));
    assertThat(ex.getMessage(), containsString("Unknown name bogus"));
  }

  public static class NoDefaultConstructorBitstring extends Asn1Bitstring {
    public NoDefaultConstructorBitstring(int size) {
      super(size, false, new String[] {});
    }
  }

  static final String JSON_VARIABLE_SIZE = """
      {
        "value": "28",
        "length": 5
      }
      """;

  static final String JSON_MISSING_LENGTH = """
      {
        "value": "28"
      }
      """;

  static final String XML_VARIABLE_SIZE = """
      <MessageContainsBitstring>
        <bits>00101</bits>
      </MessageContainsBitstring>
      """;
}
