package us.dot.its.jpo.asn.runtime.serialization;

import java.io.StringWriter;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;
import tools.jackson.dataformat.xml.ser.ToXmlGenerator;
import tools.jackson.dataformat.xml.ser.XmlSerializationContext;
import javax.xml.namespace.QName;
import us.dot.its.jpo.asn.runtime.types.Asn1Type;

/**
 * Serializer for ASN.1 "open types" which are fields without a specific type,
 * for dealing with parameterized fields with value sets of different types that
 * can be plugged in.  In this Java implementation these are represented by type parameters
 * in abstract types.
 *
 * <p>For example, the contents of the "value" field in a J2735 MessageFrame.
 *
 * <p>XER wraps open types with the type name like:
 * <pre>{@code
 *
 *     <MessageFrame>
 *         <messageId>19</messageId>
 *         <value>
 *             <SPAT>
 *                 ...
 *             </SPAT>
 *         </value>
 *     </MessageFrame>
 *
 *  }</pre>
 * <p>See XMLOpenTypeFieldValue: ITU-T Rec. X.681 (02/2021) Section 14.6.
 *
 * <p>JER does not wrap them:
 * <pre>{@code
 *
 *     {
 *         "messageId": 19,
 *         "value": {
 *             ...
 *         }
 *     }
 *
 * }</pre>
 * <p>See "Encoding of open type values", ITU-T Rec X.697 (02/2021), Sec 41.
 *
 * <p> asn1c expects wrapped JSON though</p>
 *
 * @author Ivan Yourshaw
 */
public abstract class OpenTypeSerializer<T extends Asn1Type> extends StdSerializer<T> {

    protected final QName wrapper;
    protected final QName wrapped;

    protected OpenTypeSerializer(Class<T> t, String wrapper, String wrapped) {
        super(t);
        this.wrapper = wrapper != null ? new QName(wrapper) : null;
        this.wrapped = new QName(wrapped);
    }

    @Override
    public void serialize(T t, JsonGenerator jsonGenerator, SerializationContext serializerProvider) {
        if (serializerProvider instanceof XmlSerializationContext) {
            // Wrapped XER
            var xmlGen = (ToXmlGenerator)jsonGenerator;
            if (wrapper != null) {
                xmlGen.startWrappedValue(wrapper, wrapped);
                xmlGen.writePOJO(t);
                xmlGen.finishWrappedValue(wrapper, wrapped);
            } else {
                var sw = new StringWriter();
                try (ToXmlGenerator xGen = (ToXmlGenerator)serializerProvider.createGenerator(sw)) {
                    xGen.setNextName(wrapped);
                    serializerProvider.writeValue(xGen, t);
                }
                xmlGen.writeRaw(sw.toString());
            }
        } else {
            // Wrapped JER
            jsonGenerator.writeStartObject();
            jsonGenerator.writePOJOProperty(wrapped.getLocalPart(), t);
            jsonGenerator.writeEndObject();
        }

    }
}
