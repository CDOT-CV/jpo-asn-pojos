package us.dot.its.jpo.asn.runtime.serialization;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.TreeNode;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.asn.runtime.types.Asn1Null;

@Slf4j
public class NullDeserializer<T extends Asn1Null> extends StdDeserializer<T> {

  protected Asn1Null construct() {
    return new Asn1Null();
  }

  public NullDeserializer() {
    super(Asn1Null.class);
  }

  protected NullDeserializer(Class<T> valueType) {
    super(valueType);
  }

  // Override getNullValue:
  // This is used by JER to construct the Asn1Null object instead of Java null.
  @SuppressWarnings({"unchecked"})
  @Override
  public T getNullValue(DeserializationContext deserializationContext) {
    log.trace("JSON null value, constructing a {}", handledType());
    return (T) construct();
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws JacksonException {
    // Used by XER only, JER never calls this.
    // Read the empty node
    if (log.isTraceEnabled()) {
      TreeNode node = jsonParser.objectReadContext().readTree(jsonParser);
      log.trace("node: {}", node);
    } else {
      // skip the node to prevent error from "fail on trailing tokens"
      jsonParser.skipChildren();
    }
    return (T) construct();
  }


}
