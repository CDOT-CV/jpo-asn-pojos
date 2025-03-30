package us.dot.its.jpo.asn.runtime.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.asn.runtime.types.Asn1SequenceOf;
import us.dot.its.jpo.asn.runtime.types.Asn1Type;

@Slf4j
public class RootSequenceOfSerializer<S extends Asn1Type, T extends Asn1SequenceOf<S>>
    extends StdSerializer<T> {
  protected final Class<S> itemClass;
  protected final Class<T> sequenceOfClass;

  protected RootSequenceOfSerializer(Class<S> itemClass, Class<T> sequenceOfClass) {
    super(sequenceOfClass);
    this.itemClass = itemClass;
    this.sequenceOfClass = sequenceOfClass;
  }

  @Override
  public void serialize(T sequenceOf, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    if (serializerProvider instanceof XmlSerializerProvider) {
      // XER
      // Hack needed for serializing a root-level collection to XML because
      // Jackson defaults to naming the items "item" instead of using the root name
      // of the item class, and the JacksonXmlElementWrapper and JacksonXmlProperty
      // annotations can't be used at the class level.
      var xmlGen = (ToXmlGenerator)jsonGenerator;
      var mapper = (XmlMapper)xmlGen.getCodec();
      for (var item : sequenceOf) {
        String itemXml = mapper.writeValueAsString(item);
        xmlGen.writeRaw(itemXml);
      }
    } else {
      // JER
      jsonGenerator.writeStartArray();
      for (var item : sequenceOf) {
        jsonGenerator.writeObject(item);
      }
      jsonGenerator.writeEndArray();
    }
  }
}
