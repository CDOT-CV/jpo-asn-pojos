package us.dot.its.jpo.asn.runtime.serialization;

import static us.dot.its.jpo.asn.runtime.utils.XmlUtils.unwrap;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;
import tools.jackson.dataformat.xml.ser.ToXmlGenerator;
import tools.jackson.dataformat.xml.ser.XmlSerializationContext;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.asn.runtime.types.Asn1Choice;
import us.dot.its.jpo.asn.runtime.types.Asn1SequenceOf;
import static us.dot.its.jpo.asn.runtime.serialization.Mappers.XML_MAPPER;

/**
 * Serializer for SEQUENCE-OF CHOICE types. These are unwrapped in XER, but wrapped in JER.
 *
 * @param <S> The Asn1Choice type
 * @param <T> The Asn1SequenceOf type
 */
@Slf4j
public class SequenceOfChoiceSerializer<S extends Asn1Choice, T extends Asn1SequenceOf<S>>
    extends StdSerializer<T> {

  protected final Class<S> choiceClass;
  protected final Class<T> sequenceOfClass;

  protected SequenceOfChoiceSerializer(Class<S> choiceClass, Class<T> sequenceOfClass) {
    super(sequenceOfClass);
    this.choiceClass = choiceClass;
    this.sequenceOfClass = sequenceOfClass;
  }

  @Override
  public void serialize(T sequenceOf, JsonGenerator jsonGenerator,
      SerializationContext serializerProvider) {
    if (serializerProvider instanceof XmlSerializationContext xmlProvider) {
      // XER: Choice items not wrapped
      var xmlGen = (ToXmlGenerator) jsonGenerator;

      xmlGen.writeStartArray();
      for (S choiceItem : sequenceOf) {
        log.trace(
            "SequenceOfChoiceSerializer: ChoiceClass: {}, SequenceOfClass: {}, choiceItem: {}",
            choiceClass.getName(), sequenceOfClass.getName(),
            choiceItem);
        String choiceXml = XML_MAPPER.writeValueAsString(choiceItem);
        String unwrappedXml = unwrap(choiceXml);
        xmlGen.writeRaw(unwrappedXml);
      }
      xmlGen.writeEndArray();

    } else {
      // JER: Normal, choice items are wrapped
      jsonGenerator.writePOJO(sequenceOf);
    }
  }


}
