package us.dot.its.jpo.asn.runtime.serialization;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.exc.MismatchedInputException;
import tools.jackson.dataformat.xml.XmlFactory;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.asn.runtime.types.Asn1Enumerated;
import us.dot.its.jpo.asn.runtime.types.Asn1SequenceOf;

@Slf4j
public abstract class SequenceOfEnumeratedDeserializer<S extends Enum<?> & Asn1Enumerated, T extends Asn1SequenceOf<S>>
    extends StdDeserializer<T> {

  protected final Class<T> thisClass;
  protected final Class<S> enumClass;

  protected abstract S[] listEnumValues();

  protected abstract T construct();

  protected SequenceOfEnumeratedDeserializer(Class<T> sequenceOfEnumType, Class<S> enumType) {
    super(sequenceOfEnumType);
    this.thisClass = sequenceOfEnumType;
    this.enumClass = enumType;
  }

  @Override
  public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws JacksonException {
    T result = null;
    if (deserializationContext.tokenStreamFactory() instanceof XmlFactory) {
      // XER
      // Unwrapped enum items, each an empty element named for the enum value
      result = construct();
      JsonToken token = jsonParser.currentToken();
      if (token == JsonToken.START_OBJECT) {
        token = jsonParser.nextToken();
      } else if (token != JsonToken.PROPERTY_NAME) {
        // Empty element, no items
        return result;
      }
      while (token != JsonToken.END_OBJECT) {
        if (token != JsonToken.PROPERTY_NAME) {
          throw MismatchedInputException.from(jsonParser, thisClass,
              "Expected an enumerated element, found " + token);
        }
        final String name = jsonParser.currentName();
        log.trace("SequenceOfEnumeratedDeserializer: name: {}", name);
        final JsonToken value = jsonParser.nextToken();
        if (value != null && value.isStructStart()) {
          jsonParser.skipChildren();
        }
        result.add(findEnum(jsonParser, name));
        token = jsonParser.nextToken();
      }
    } else {
      // JER is simpler, pass though
      result = jsonParser.objectReadContext().readValue(jsonParser, thisClass);
    }
    return result;
  }

  private S findEnum(JsonParser jsonParser, String name) {
    for (S enumValue : listEnumValues()) {
      if (Objects.equals(enumValue.getName(), name)) {
        return enumValue;
      }
    }
    throw MismatchedInputException.from(jsonParser, enumClass,
        String.format("Invalid enum value: %s. Must be one of: %s", name,
            Stream.of(listEnumValues()).map(Asn1Enumerated::getName)
                .collect(Collectors.joining(", "))));
  }

}
