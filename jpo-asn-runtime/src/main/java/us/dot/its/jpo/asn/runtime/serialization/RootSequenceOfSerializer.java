package us.dot.its.jpo.asn.runtime.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
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
public class RootSequenceOfSerializer
    extends StdSerializer<Asn1SequenceOf<Asn1Type>> {


  protected RootSequenceOfSerializer() {
    super(Asn1SequenceOf.class, true);
  }

  @Override
  public void serialize(Asn1SequenceOf<Asn1Type> sequenceOf, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    if (serializerProvider instanceof XmlSerializerProvider) {
      // XER
      // Hack needed for serializing a root-level collection to XML because
      // Jackson defaults to naming the items "item" instead of using the root name
      // of the item class, and the JacksonXmlElementWrapper and JacksonXmlProperty
      // annotations can't be used at the class level.
      var xmlGen = (ToXmlGenerator)jsonGenerator;
      var mapper = (XmlMapper)xmlGen.getCodec();
      xmlGen.writeStartArray();
      for (Asn1Type item : sequenceOf) {
        String itemXml = mapper.writeValueAsString(item);
        xmlGen.writeRaw(itemXml);
      }
      xmlGen.writeEndArray();
    } else {
      // JER
      jsonGenerator.writeStartArray();
      for (Asn1Type item : sequenceOf) {
        jsonGenerator.writeObject(item);
      }
      jsonGenerator.writeEndArray();
    }
  }

}
