package us.dot.its.jpo.asn.runtime.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import us.dot.its.jpo.asn.runtime.types.Asn1Bitstring;

import java.io.IOException;

/**
 * Serializer for ASN.1 Bitstring types to XER, JER, or a custom format with human-readable named values.
 * @author Ivan Yourshaw
 */
public class BitstringSerializer extends StdSerializer<Asn1Bitstring> {

    public BitstringSerializer() {
        super(Asn1Bitstring.class);
    }

    @Override
    public void serialize(Asn1Bitstring asn1Bitstring, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (serializerProvider instanceof XmlSerializerProvider) {
            // XER serializes bitstrings as binary (strings of "1"s and "0"s)
            jsonGenerator.writeString(asn1Bitstring.binaryString());
        } else {
            serializeJson(asn1Bitstring, jsonGenerator);
        }
    }

    private void serializeJson(Asn1Bitstring asn1Bitstring, JsonGenerator jsonGenerator) throws IOException {
        var codec = jsonGenerator.getCodec();
        if (codec instanceof OdeCustomJsonMapper customMapper && customMapper.isHumanReadableJsonBitstrings()) {
            serializeJsonMap(asn1Bitstring, jsonGenerator);
        } else {
            serializeJer(asn1Bitstring, jsonGenerator);
        }
    }

    private void serializeJsonMap(Asn1Bitstring asn1Bitstring, JsonGenerator jsonGenerator) throws IOException {
        // ODE JSON dialect serializes bitstrings as verbose maps
        jsonGenerator.writeStartObject();
        for (int i = 0; i < asn1Bitstring.getLowerBound(); i++) {
            String name = asn1Bitstring.name(i);
            boolean isSet = asn1Bitstring.get(i);
            jsonGenerator.writeBooleanField(name, isSet);
        }
        jsonGenerator.writeEndObject();
    }

    private void serializeJer(Asn1Bitstring asn1Bitstring, JsonGenerator jsonGenerator) throws IOException {
        // JER serializes BIT STRING values as hex strings.
        //
        // Serialize as a simple JSON hex string if the size constraint is fixed.
        //
        // If the size constraint has an upper bound that is different from the lower bound, or
        // if the size constraint is extensible, serialize as:
        //
        // {
        //   "value": "80",
        //   "length": 1
        // }
        //
        // Refer to Rec. ITU-T X.697 (02/2021):
        //   * Sec 7.2.1 - states that only "non-extensible" size constraints on BIT STRINGs are JER-visible, and
        //   * Sec 7.2.2 - states that extensible subtype constraints are NOT JER-visible
        //   * Sec 24.1 to 24.3 - Defines the encoding for fixed and variable-length BIT STRINGs
        //
        if (asn1Bitstring.hasVariableSize()) {
            // Variable-size format
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("value", asn1Bitstring.hexString());
            jsonGenerator.writeNumberField("length", asn1Bitstring.getActualSize());
            jsonGenerator.writeEndObject();
        } else {
            // Fixed size format
            jsonGenerator.writeString(asn1Bitstring.hexString());
        }
    }
}
