package us.dot.its.jpo.asn.runtime.serialization;

import static us.dot.its.jpo.asn.runtime.utils.XmlUtils.extractXmlList;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.dataformat.xml.deser.FromXmlParser;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.asn.runtime.types.Asn1Choice;
import us.dot.its.jpo.asn.runtime.types.Asn1SequenceOf;
import static us.dot.its.jpo.asn.runtime.serialization.Mappers.XML_MAPPER;


/**
 * Deserializer for SEQUENCE-OF CHOICE types. These are unwrapped in XER, but wrapped in JER.
 *
 * @param <S> The Asn1Choice type
 * @param <T> The Asn1SequenceOf type
 */
@Slf4j
public abstract class SequenceOfChoiceDeserializer<S extends Asn1Choice, T extends Asn1SequenceOf<S>>
    extends StdDeserializer<T> {

  protected final Class<S> choiceClass;
  protected final Class<T> sequenceOfClass;

  protected abstract T construct();

  protected SequenceOfChoiceDeserializer(Class<S> choiceClass, Class<T> sequenceOfClass) {
    super(sequenceOfClass);
    this.choiceClass = choiceClass;
    this.sequenceOfClass = sequenceOfClass;
  }


  @Override
  public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws JacksonException {
    T result = construct();
    if (jsonParser instanceof FromXmlParser xmlParser) {

      // XML: expects unwrapped choice items.
      // We need to do all this because simple xmlMapper.readTree doesn't preserve the
      // original order of sequence items.

      List<String> choiceXmlList = extractXmlList(xmlParser);

      // Wrap and deserialize each choice item
      for (String choiceXml : choiceXmlList) {
        log.trace("SequenceOfChoiceDeserializer: choiceXml: {}", choiceXml);
        var wrapped = String.format("<%s>%s</%s>", choiceClass.getSimpleName(), choiceXml,
            choiceClass.getSimpleName());
        S choice = XML_MAPPER.readValue(wrapped, choiceClass);
        result.add(choice);
      }
    } else {
      // JSON is easier! It expects wrapped choice items, pass through as normal
      result = jsonParser.objectReadContext().readValue(jsonParser, sequenceOfClass);
    }
    return result;
  }


}
