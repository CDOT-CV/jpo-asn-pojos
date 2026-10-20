package us.dot.its.jpo.asn.runtime.serialization;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;
import tools.jackson.dataformat.xml.ser.XmlSerializationContext;
import us.dot.its.jpo.asn.runtime.types.Asn1Enumerated;
import us.dot.its.jpo.asn.runtime.types.Asn1SequenceOf;

public class SequenceOfEnumeratedSerializer<S extends Asn1Enumerated, T extends Asn1SequenceOf<S>>
  extends StdSerializer<T> {

  protected final Class<S> enumClass;
  protected final Class<T> sequenceOfClass;

  protected SequenceOfEnumeratedSerializer(Class<S> enumClass, Class<T> sequenceOfClass) {
    super(sequenceOfClass);
    this.enumClass = enumClass;
    this.sequenceOfClass = sequenceOfClass;
  }

  @Override
  public void serialize(T sequenceOf, JsonGenerator jsonGenerator, SerializationContext serializerProvider){
    if (serializerProvider instanceof XmlSerializationContext xmlProvider) {
      // XER: write unwrapped
      jsonGenerator.writeStartArray();
      for (var enumItem : sequenceOf) {
        jsonGenerator.writeRaw(String.format("<%s/>", enumItem.getName()));
      }
      jsonGenerator.writeEndArray();
    } else {
      // JER: Normal, pass through
      jsonGenerator.writePOJO(sequenceOf);
    }
  }
}
