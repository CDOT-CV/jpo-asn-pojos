package us.dot.its.jpo.asn.runtime.serialization;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;
import tools.jackson.dataformat.xml.ser.ToXmlGenerator;
import tools.jackson.dataformat.xml.ser.XmlSerializationContext;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.asn.runtime.types.Asn1SequenceOf;
import us.dot.its.jpo.asn.runtime.types.Asn1Type;

@Slf4j
public class SequenceOfOpenTypeSerializer<S extends Asn1Type, T extends Asn1SequenceOf<S>>
    extends StdSerializer<T> {

  protected final Class<S> itemClass;
  protected final Class<T> sequenceOfClass;

  protected SequenceOfOpenTypeSerializer(Class<S> itemClass, Class<T> sequenceOfClass) {
    super(sequenceOfClass);
    this.itemClass = itemClass;
    this.sequenceOfClass = sequenceOfClass;
  }

  @Override
  public void serialize(T sequenceOf, JsonGenerator jsonGenerator, SerializationContext serializerProvider) {
    if (serializerProvider instanceof XmlSerializationContext) {
      // XER: Write each item sequentially without wrapping
      var xmlGen = (ToXmlGenerator)jsonGenerator;
      var mapper = (ObjectMapper)xmlGen.objectWriteContext();
      for (var item : sequenceOf) {
        String itemXml = mapper.writeValueAsString(item);
        log.trace("itemXml: {}", itemXml);
        xmlGen.writeRaw(itemXml);
      }
    } else {
      // JER: The default works
      jsonGenerator.writePOJO(sequenceOf);
    }

  }
}
