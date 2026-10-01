package us.dot.its.jpo.asn.runtime.serialization;

import static us.dot.its.jpo.asn.runtime.utils.XmlUtils.extractXmlList;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.dataformat.xml.deser.FromXmlParser;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.asn.runtime.types.Asn1Enumerated;
import us.dot.its.jpo.asn.runtime.types.Asn1SequenceOf;
import static us.dot.its.jpo.asn.runtime.serialization.Mappers.XML_MAPPER;

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
    if (jsonParser instanceof FromXmlParser xmlParser) {
      // XER
      // Unwrapped enum items
      result = construct();

      List<String> enumXmlList = extractXmlList(xmlParser);

      for (String enumXml : enumXmlList) {
        log.trace("SequenceOfEnumeratedDeserializer: enumXml: {}", enumXml);
        var wrapped = String.format("<%s>%s</%s>", enumClass.getSimpleName(), enumXml,
            enumClass.getSimpleName());
        S enumerated = XML_MAPPER.readValue(wrapped, enumClass);
        result.add(enumerated);

      }
    } else {
      // JER is simpler, pass though
      result = jsonParser.objectReadContext().readValue(jsonParser, thisClass);
    }
    return result;
  }

}
