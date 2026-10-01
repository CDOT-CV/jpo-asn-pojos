package us.dot.its.jpo.asn.runtime.serialization;

import static us.dot.its.jpo.asn.runtime.utils.XmlUtils.unwrap;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;
import tools.jackson.dataformat.xml.ser.ToXmlGenerator;
import tools.jackson.dataformat.xml.ser.XmlSerializationContext;
import javax.xml.namespace.QName;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.asn.runtime.types.Asn1Integer;
import us.dot.its.jpo.asn.runtime.types.Asn1SequenceOf;

/**
 * Serializer for nested, anonymous SEQUENCE-OF types
 * @param <T> The Sequence-of type
 * @author Ivan Yourshaw
 */
@Slf4j
public class NestedSequenceOfSerializer<T extends Asn1SequenceOf<?>> extends StdSerializer<T> {

    protected final QName wrapped;

    protected NestedSequenceOfSerializer(Class<T> t, String wrapped) {
        super(t);
        this.wrapped = new QName(wrapped);
    }

    @Override
    public void serialize(T t, JsonGenerator jsonGenerator, SerializationContext serializerProvider) {
        try {
            if (serializerProvider instanceof XmlSerializationContext) {
                // Wrapped XER
                var xmlGen = (ToXmlGenerator) jsonGenerator;
                var mapper = (ObjectMapper) xmlGen.objectWriteContext();

                if ("INTEGER".equals(wrapped.getLocalPart())) {
                    xmlGen.writeStartArray();
                    for (var item : t) {
                        xmlGen.writeRaw(String.format("<INTEGER>%s</INTEGER>", ((Asn1Integer)item).getValue()));
                    }
                    xmlGen.writeEndArray();
                } else {
                    // Works for sequence
                    for (var item : t) {
                        xmlGen.writeRaw(String.format("<%s>", wrapped));

                        final String itemXml = mapper.writeValueAsString(item);
                        final String strippedXml = unwrap(itemXml);
                        xmlGen.writeRaw(strippedXml);

                        xmlGen.writeRaw(String.format("</%s>", wrapped));
                    }
                }



            } else {
                // Pass through JER
                jsonGenerator.writePOJO(t);
            }
        } catch (Exception ex) {
            log.error("Error serializing", ex);
        }
    }
}
