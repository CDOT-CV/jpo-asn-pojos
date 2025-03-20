package us.dot.its.jpo.asn.runtime.serialization;

import static us.dot.its.jpo.asn.runtime.utils.XmlUtils.unwrap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import java.io.IOException;
import javax.xml.namespace.QName;
import us.dot.its.jpo.asn.runtime.types.Asn1SequenceOf;

/**
 * Serializer for nested, anonymous SEQUENCE-OF types
 * @param <T> The Sequence-of type
 * @author Ivan Yourshaw
 */
public class NestedSequenceOfSerializer<T extends Asn1SequenceOf<?>> extends StdSerializer<T> {

    protected final QName wrapped;

    protected NestedSequenceOfSerializer(Class<T> t, String wrapped) {
        super(t);
        this.wrapped = new QName(wrapped);
    }

    @Override
    public void serialize(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (serializerProvider instanceof XmlSerializerProvider) {
            // Wrapped XER
            var xmlGen = (ToXmlGenerator)jsonGenerator;
            var mapper = (ObjectMapper)xmlGen.getCodec();
            for (var item : t) {

                xmlGen.writeRaw(String.format("<%s>", wrapped));

                final String itemXml = mapper.writeValueAsString(item);
                final String strippedXml = unwrap(itemXml);
                xmlGen.writeRaw(strippedXml);

                xmlGen.writeRaw(String.format("</%s>", wrapped));
            }

        } else {
            // Pass through JER
            jsonGenerator.writeObject(t);
        }
    }
}
