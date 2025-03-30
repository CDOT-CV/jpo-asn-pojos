package us.dot.its.jpo.asn.runtime.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HexFormat;
import lombok.Getter;

/**
 * Represents an ASN.1 OCTET STRING type, and is the base class for OCTET STRING type assignments.
 */
public class Asn1OctetString implements Asn1Type {

    @Getter
    protected final int minLength;

    @Getter
    protected final int maxLength;

    @JsonIgnore
    protected byte[] octets;

    @JsonValue
    public String getValue() {
        HexFormat hexFormat = HexFormat.of().withUpperCase();
        if (octets != null) {
            return hexFormat.formatHex(octets);
        } else {
            return "";
        }
    }

    public void setValue(String value) {
        try {
            validate(value);
            HexFormat hexFormat = HexFormat.of();
            this.octets = hexFormat.parseHex(value.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    String.format("Hex String '%s' is not valid", value), e);
        }
    }

    @JsonIgnore
    public byte[] getOctets() {
        return octets;
    }

    @JsonIgnore
    public void setOctets(byte[] octets) {
        this.octets = octets;
    }

    /**
     * Default constructor creates octet string with unrestricted length
     */
    public Asn1OctetString() {
        this.minLength = 0;
        this.maxLength = Integer.MAX_VALUE;
    }

    public Asn1OctetString(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    protected void validate(String aValue) throws Exception {
        if (aValue == null) {
            throw new Exception("hex string is null");
        }
        // Size of hex format string can be 2 * byte size
        int trimmedLength = aValue.trim().length();
        long minLength2 = 2L * minLength;
        long maxLength2 = 2L * maxLength; // prevent 2*Integer.MAX_VALUE is negative
        boolean validLength = (trimmedLength >= minLength2) && (trimmedLength <= maxLength2);
        if (!validLength) {
            throw new Exception(String.format("Hex string length out of bounds, %s, number of bytes must be between" +
                " %d and %d", aValue, minLength, maxLength));
        }
    }

    @Override
    public String toString() {
        return getValue();
    }

}
