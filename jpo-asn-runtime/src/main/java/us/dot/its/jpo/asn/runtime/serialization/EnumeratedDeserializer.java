package us.dot.its.jpo.asn.runtime.serialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import java.io.IOException;
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
      throws IOException, JacksonException {
    String name = null;
    if (jsonParser instanceof FromXmlParser xmlParser) {
      // XML
      // The enum in BASIC-XER is an empty element, so Jackson thinks it's an object with a key
      // of that name with no value
      TreeNode node = xmlParser.getCodec().readTree(xmlParser);
      var iterator = node.fieldNames();
      if (iterator.hasNext()) {
        name = node.fieldNames().next();
      }
    } else {
      // JSON
      // Behaves normally: The enum name is the text
      name = jsonParser.getText();
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
