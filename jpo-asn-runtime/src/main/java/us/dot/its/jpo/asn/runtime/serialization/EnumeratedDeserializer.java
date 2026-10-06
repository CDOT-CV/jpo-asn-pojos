package us.dot.its.jpo.asn.runtime.serialization;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.TreeNode;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.exc.MismatchedInputException;
import tools.jackson.dataformat.xml.XmlFactory;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import us.dot.its.jpo.asn.runtime.types.Asn1Enumerated;

@Slf4j
public abstract class EnumeratedDeserializer<T extends Enum<?> & Asn1Enumerated> extends
    StdDeserializer<T> {

  protected abstract T[] listEnumValues();

  protected EnumeratedDeserializer(Class<T> valueClass) {
    super(valueClass);
  }

  @Override
  public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws JacksonException {
    String name = null;
    if (deserializationContext.tokenStreamFactory() instanceof XmlFactory) {
      // XML
      // The enum in BASIC-XER is an empty element, so Jackson thinks it's an object with a key
      // of that name with no value
      TreeNode node = jsonParser.readValueAsTree();
      var propNames = node.propertyNames();
      for (var propName : propNames) {
        name = propName;
      }
    } else {
      // JSON
      // Behaves normally: The enum name is the text
      name = jsonParser.getString();
    }

    // Return null if the text actually is null or empty
    if (StringUtils.isBlank(name)) {
      return null;
    }

    for (T enumValue : listEnumValues()) {
      if (Objects.equals(enumValue.getName(), name)) {
        return enumValue;
      }
    }

    throw MismatchedInputException.from(jsonParser, getValueType(),
        String.format("Invalid enum value: %s. Must be one of: %s", name,
            Stream.of(listEnumValues()).map(Asn1Enumerated::getName).collect(Collectors.joining(", "))));
  }
}
