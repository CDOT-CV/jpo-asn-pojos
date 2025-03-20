package us.dot.its.jpo.asn.runtime.serialization;

import static us.dot.its.jpo.asn.runtime.utils.XmlUtils.extractXmlList;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
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
      throws IOException, JacksonException {
    T result = null;
    if (jsonParser instanceof FromXmlParser xmlParser) {
      // XER
      // Unwrapped enum items
      result = construct();

      List<String> enumXmlList = extractXmlList(xmlParser);

      XmlMapper xmlMapper = (XmlMapper) xmlParser.getCodec();
      for (String enumXml : enumXmlList) {
        log.trace("SequenceOfEnumeratedDeserializer: enumXml: {}", enumXml);
        var wrapped = String.format("<%s>%s</%s>", enumClass.getSimpleName(), enumXml,
            enumClass.getSimpleName());
        S enumerated = xmlMapper.readValue(wrapped, enumClass);
        result.add(enumerated);

      }
    } else {
      // JER is simpler, pass though
      result = jsonParser.getCodec().readValue(jsonParser, thisClass);
    }
    return result;
  }

}
