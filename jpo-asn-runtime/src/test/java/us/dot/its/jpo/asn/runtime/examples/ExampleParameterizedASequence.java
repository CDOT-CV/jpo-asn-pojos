package us.dot.its.jpo.asn.runtime.examples;

import com.fasterxml.jackson.annotation.JsonRootName;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;
import us.dot.its.jpo.asn.runtime.serialization.OpenTypeDeserializer;
import us.dot.its.jpo.asn.runtime.serialization.OpenTypeSerializer;

@JsonRootName("ExampleParameterized")
@JsonDeserialize(using = ValueDeserializer.None.class)
public class ExampleParameterizedASequence extends ExampleParameterized<ASequence> {

  public ExampleParameterizedASequence() {
    super(1);
  }

  @Override
  @JsonSerialize(using = ASequenceValueSerializer.class)
  public ASequence getValue() {
    return super.getValue();
  }

  @Override
  @JsonDeserialize(using = ASequenceValueDeserializer.class)
  public void setValue(ASequence value) {
    super.setValue(value);
  }

  public static class ASequenceValueSerializer extends OpenTypeSerializer<ASequence> {
    public ASequenceValueSerializer() {
      super(ASequence.class, "value", "ASequence");
    }
  }

  public static class ASequenceValueDeserializer extends OpenTypeDeserializer<ASequence> {
    public ASequenceValueDeserializer() {
      super(ASequence.class, "ASequence");
    }
  }
}
