package us.dot.its.jpo.asn.runtime.examples;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import tools.jackson.databind.annotation.JsonDeserialize;
import us.dot.its.jpo.asn.runtime.annotations.Asn1ParameterizedTypes;
import us.dot.its.jpo.asn.runtime.annotations.Asn1ParameterizedTypes.IdType;
import us.dot.its.jpo.asn.runtime.serialization.ParameterizedTypeDeserializer;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;

/**
 * Parameterized SEQUENCE example, modeled after the generated J2735 MessageFrame.
 */
@JsonPropertyOrder({"messageId", "value"})
@Asn1ParameterizedTypes(
    idProperty = "messageId",
    idType = IdType.INTEGER,
    valueProperty = "value",
    value = {
        @Asn1ParameterizedTypes.Type(value = ExampleParameterizedASequence.class, intId = 1),
        @Asn1ParameterizedTypes.Type(value = ExampleParameterizedSequenceOfChoice.class, intId = 2)
    })
@JsonDeserialize(using = ExampleParameterized.ExampleParameterizedDeserializer.class)
public abstract class ExampleParameterized<TValue> extends Asn1Sequence {

  protected AInteger messageId;
  protected TValue value;

  protected ExampleParameterized(int id) {
    this.messageId = new AInteger(id);
  }

  @JsonProperty("messageId")
  public AInteger getMessageId() {
    return messageId;
  }

  @JsonProperty("messageId")
  public void setMessageId(AInteger messageId) {
    this.messageId = messageId;
  }

  @JsonProperty("value")
  public TValue getValue() {
    return value;
  }

  @JsonProperty("value")
  public void setValue(TValue value) {
    this.value = value;
  }

  @SuppressWarnings("rawtypes")
  public static class ExampleParameterizedDeserializer
      extends ParameterizedTypeDeserializer<ExampleParameterized> {

    public ExampleParameterizedDeserializer() {
      super(ExampleParameterized.class);
    }
  }
}
