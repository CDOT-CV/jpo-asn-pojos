package us.dot.its.jpo.asn.runtime.examples;

import com.fasterxml.jackson.annotation.JsonRootName;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;
import us.dot.its.jpo.asn.runtime.serialization.OpenTypeDeserializer;
import us.dot.its.jpo.asn.runtime.serialization.OpenTypeSerializer;

@JsonRootName("ExampleParameterized")
@JsonDeserialize(using = ValueDeserializer.None.class)
public class ExampleParameterizedSequenceOfChoice
    extends ExampleParameterized<MessageContainsSequenceOfChoice> {

  public ExampleParameterizedSequenceOfChoice() {
    super(2);
  }

  @Override
  @JsonSerialize(using = SequenceOfChoiceValueSerializer.class)
  public MessageContainsSequenceOfChoice getValue() {
    return super.getValue();
  }

  @Override
  @JsonDeserialize(using = SequenceOfChoiceValueDeserializer.class)
  public void setValue(MessageContainsSequenceOfChoice value) {
    super.setValue(value);
  }

  public static class SequenceOfChoiceValueSerializer
      extends OpenTypeSerializer<MessageContainsSequenceOfChoice> {
    public SequenceOfChoiceValueSerializer() {
      super(MessageContainsSequenceOfChoice.class, "value", "MessageContainsSequenceOfChoice");
    }
  }

  public static class SequenceOfChoiceValueDeserializer
      extends OpenTypeDeserializer<MessageContainsSequenceOfChoice> {
    public SequenceOfChoiceValueDeserializer() {
      super(MessageContainsSequenceOfChoice.class, "MessageContainsSequenceOfChoice");
    }
  }
}
