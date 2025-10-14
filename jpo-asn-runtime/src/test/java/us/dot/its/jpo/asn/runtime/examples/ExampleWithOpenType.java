package us.dot.its.jpo.asn.runtime.examples;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import us.dot.its.jpo.asn.runtime.serialization.OpenTypeDeserializer;
import us.dot.its.jpo.asn.runtime.serialization.OpenTypeSerializer;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("MessageFrame")
public class ExampleWithOpenType extends Asn1Sequence {

  @Getter
  @Setter
  private AInteger messageId;

  private ASequence value;

  @JsonSerialize(using = ValueSerializer.class)
  public ASequence getValue() {
    return value;
  }

  @JsonDeserialize(using = ValueDeserializer.class)
  public void setValue(ASequence value) {
    this.value = value;
  }

  public static class ValueSerializer extends OpenTypeSerializer<ASequence> {
    protected ValueSerializer() {
      super(ASequence.class, "value", "ASequence");
    }
  }

  public static class ValueDeserializer extends OpenTypeDeserializer<ASequence> {
    protected ValueDeserializer() {
      super(ASequence.class, "ASequence");
    }
  }

}
