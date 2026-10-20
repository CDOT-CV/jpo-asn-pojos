package us.dot.its.jpo.asn.runtime.serialization;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;
import tools.jackson.dataformat.xml.ser.XmlSerializationContext;
import us.dot.its.jpo.asn.runtime.types.Asn1Boolean;

public class BooleanSerializer extends StdSerializer<Asn1Boolean> {

    protected BooleanSerializer() {
        super(Asn1Boolean.class);
    }

    @Override
    public void serialize(Asn1Boolean asn1Boolean, JsonGenerator jsonGenerator, SerializationContext serializerProvider) {
        if (serializerProvider instanceof XmlSerializationContext) {
            // XER uses <true/> and <false/> for booleans
            jsonGenerator.writeStartObject();
            jsonGenerator.writeRaw(String.format("<%s/>", asn1Boolean.getValue()));
            jsonGenerator.writeEndObject();
        } else {
            // JER
            jsonGenerator.writeBoolean(asn1Boolean.getValue());
        }
    }
}
