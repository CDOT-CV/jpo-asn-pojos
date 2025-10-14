package us.dot.its.jpo.asn.runtime.serialization;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.notNullValue;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import us.dot.its.jpo.asn.runtime.examples.ExampleBitstring;
import us.dot.its.jpo.asn.runtime.examples.ExampleBitstringFewNamedBits;

public class OdeCustomJsonMapperTest {

  @Test
  public void serializeHumanReadable() throws JsonProcessingException {
    var mapper = new OdeCustomJsonMapper(true);
    ExampleBitstring ebs = new ExampleBitstring();
    ebs.fromBinaryString(BINARY_STRING);
    final String str = mapper.writeValueAsString(ebs);
    assertThat(str, jsonEquals(HUMAN_READABLE_JSON));
  }

  @Test
  public void deserializeHumanReadable() throws JsonProcessingException {
    var mapper = new OdeCustomJsonMapper(true);
    ExampleBitstring ebs = mapper.readValue(HUMAN_READABLE_JSON, ExampleBitstring.class);
    assertThat(ebs, notNullValue());
    assertThat(ebs.binaryString(), equalTo(BINARY_STRING));
    assertThat(ebs.hexString(), equalToIgnoringCase(HEX_STRING));
  }

  @Test
  public void serializeHex() throws JsonProcessingException {
    var mapper = new OdeCustomJsonMapper(false);
    ExampleBitstring ebs = new ExampleBitstring();
    ebs.fromBinaryString(BINARY_STRING);
    final String str = mapper.writeValueAsString(ebs);
    assertThat(str, equalToIgnoringCase(HEX_JSON));
  }

  @Test
  public void deserializeHex() throws JsonProcessingException {
    var mapper = new OdeCustomJsonMapper(false);
    ExampleBitstring ebs = mapper.readValue(HEX_JSON, ExampleBitstring.class);
    assertThat(ebs, notNullValue());
    assertThat(ebs.binaryString(), equalTo(BINARY_STRING));
    assertThat(ebs.hexString(), equalToIgnoringCase(HEX_STRING));
  }

  @Test
  public void serializeHumanReadable_FewNamedBits() throws JsonProcessingException {
    var mapper = new OdeCustomJsonMapper(true);
    var ebs = new ExampleBitstringFewNamedBits();
    ebs.fromBinaryString(BINARY_STRING_FEW_NAMED_BITS);
    final String str = mapper.writeValueAsString(ebs);
    assertThat(str, jsonEquals(HUMAN_READABLE_JSON_FEW_NAMED_BITS));
  }

  @Test
  public void deserializeHumanReadable_FewNamedBits() throws JsonProcessingException {
    var mapper = new OdeCustomJsonMapper(true);
    var ebs = mapper.readValue(HUMAN_READABLE_JSON_FEW_NAMED_BITS, ExampleBitstringFewNamedBits.class);
    assertThat(ebs, notNullValue());
    assertThat(ebs.binaryString(), equalTo(BINARY_STRING_FEW_NAMED_BITS));
    assertThat(ebs.hexString(), equalToIgnoringCase(HEX_STRING_FEW_NAMED_BITS));
  }

  @Test
  public void serializeHex_FewNamedBits() throws JsonProcessingException {
    var mapper = new OdeCustomJsonMapper(false);
    var ebs = new ExampleBitstringFewNamedBits();
    ebs.fromBinaryString(BINARY_STRING_FEW_NAMED_BITS);
    final String str = mapper.writeValueAsString(ebs);
    assertThat(str, equalToIgnoringCase(HEX_JSON_FEW_NAMED_BITS));
  }

  @Test
  public void deserializeHex_FewNamedBits() throws JsonProcessingException {
    var mapper = new OdeCustomJsonMapper(false);
    var ebs = mapper.readValue(HEX_JSON_FEW_NAMED_BITS, ExampleBitstringFewNamedBits.class);
    assertThat(ebs, notNullValue());
    assertThat(ebs.binaryString(), equalTo(BINARY_STRING_FEW_NAMED_BITS));
    assertThat(ebs.hexString(), equalToIgnoringCase(HEX_STRING_FEW_NAMED_BITS));
  }

  static final String BINARY_STRING = "1111111111111111";
  static final String BINARY_STRING_FEW_NAMED_BITS = "1111000000000000";


  static final String HEX_STRING = "FFFF";
  static final String HEX_STRING_FEW_NAMED_BITS = "F000";

  static final String HEX_JSON = "\"" + HEX_STRING + "\"";
  static final String HEX_JSON_FEW_NAMED_BITS = "\"" + HEX_STRING_FEW_NAMED_BITS + "\"";

  static final String HUMAN_READABLE_JSON = """
      {
          "from000-0to022-5degrees": true,
          "from022-5to045-0degrees": true,
          "from045-0to067-5degrees": true,
          "from067-5to090-0degrees": true,
          "from090-0to112-5degrees": true,
          "from112-5to135-0degrees": true,
          "from135-0to157-5degrees": true,
          "from157-5to180-0degrees": true,
          "from180-0to202-5degrees": true,
          "from202-5to225-0degrees": true,
          "from225-0to247-5degrees": true,
          "from247-5to270-0degrees": true,
          "from270-0to292-5degrees": true,
          "from292-5to315-0degrees": true,
          "from315-0to337-5degrees": true,
          "from337-5to360-0degrees": true
      }
      """;

  static final String HUMAN_READABLE_JSON_FEW_NAMED_BITS = """
      {
        "sidewalk-RevocableLane": true,
        "bicyleUseAllowed": true,
        "isSidewalkFlyOverLane": true,
        "walkBikes": true
      }
      """;



}
