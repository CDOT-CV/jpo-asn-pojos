package us.dot.its.jpo.asn.runtime.serialization;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import us.dot.its.jpo.asn.runtime.types.Asn1CharacterString;
import us.dot.its.jpo.asn.runtime.types.IA5String;

public class CharacterStringSerializerTest {

  // Character strings serialize through @JsonValue by default, so register the serializer
  // explicitly to test it
  @Test
  public void canSerializeJson() {
    var module = new SimpleModule().addSerializer(Asn1CharacterString.class,
        new CharacterStringSerializer());
    var mapper = JsonMapper.builder().addModule(module).build();
    assertThat(mapper.writeValueAsString(new IA5String("hello")), jsonEquals("\"hello\""));
  }
}
