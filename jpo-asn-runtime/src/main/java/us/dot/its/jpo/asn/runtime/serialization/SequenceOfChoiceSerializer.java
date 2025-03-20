package us.dot.its.jpo.asn.runtime.serialization;

import static us.dot.its.jpo.asn.runtime.utils.XmlUtils.unwrap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.asn.runtime.types.Asn1Choice;
import us.dot.its.jpo.asn.runtime.types.Asn1SequenceOf;

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
      SerializerProvider serializerProvider) throws IOException {
    if (serializerProvider instanceof XmlSerializerProvider xmlProvider) {
      // XER: Choice items not wrapped
      var xmlGen = (ToXmlGenerator) jsonGenerator;
      var mapper = (ObjectMapper)xmlGen.getCodec();

      xmlGen.writeStartArray();
      for (S choiceItem : sequenceOf) {
        log.trace(
            "SequenceOfChoiceSerializer: ChoiceClass: {}, SequenceOfClass: {}, choiceItem: {}",
            choiceClass.getName(), sequenceOfClass.getName(),
            choiceItem);
        String choiceXml = mapper.writeValueAsString(choiceItem);
        String unwrappedXml = unwrap(choiceXml);
        xmlGen.writeRaw(unwrappedXml);
      }
      xmlGen.writeEndArray();

    } else {
      // JER: Normal, choice items are wrapped
      jsonGenerator.writeObject(sequenceOf);
    }
  }


}
