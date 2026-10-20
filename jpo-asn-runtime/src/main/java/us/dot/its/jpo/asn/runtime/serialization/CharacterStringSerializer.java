package us.dot.its.jpo.asn.runtime.serialization;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;
import us.dot.its.jpo.asn.runtime.types.Asn1CharacterString;

public class CharacterStringSerializer extends StdSerializer<Asn1CharacterString> {

    protected CharacterStringSerializer() {
        super(Asn1CharacterString.class);
    }

    @Override
    public void serialize(Asn1CharacterString asn1CharacterString, JsonGenerator jsonGenerator, SerializationContext serializerProvider) {
        jsonGenerator.writeString(asn1CharacterString.getValue());
    }
}
