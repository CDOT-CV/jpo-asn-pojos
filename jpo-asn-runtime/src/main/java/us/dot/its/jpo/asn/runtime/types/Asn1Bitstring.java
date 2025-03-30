package us.dot.its.jpo.asn.runtime.types;

import static us.dot.its.jpo.asn.runtime.utils.BitUtils.reverseBits;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.BitSet;
import java.util.HexFormat;
import us.dot.its.jpo.asn.runtime.serialization.BitstringSerializer;

/**
 * Base class for ASN.1 BIT STRING types.
 */
@JsonSerialize(using = BitstringSerializer.class)
public abstract class Asn1Bitstring implements Asn1Type {

    final BitSet bits;

    // Lower bound
    final int size;

    final int upperBound;

    final boolean hasExtensionMarker;
    final String[] names;

    public Asn1Bitstring(int size, boolean hasExtensionMarker, String[] names) {
        this.size = size;
        this.upperBound = size;
        this.hasExtensionMarker = hasExtensionMarker;
        this.bits = new BitSet(size);
        this.names = names;
    }

    public Asn1Bitstring(int lowerBound, int upperBound, boolean hasExtensionMarker, String[] names) {
        this.size = lowerBound;
        this.upperBound = upperBound;
        this.hasExtensionMarker = hasExtensionMarker;
        this.bits = new BitSet(size);
        this.names = names;
    }

    public int size() {
        return size;
    }

    /**
     * Indicates whether the SIZE constraint of this BIT STRING has an extension marker
     * @return whether the size of the BIT STRING may be extended in the future
     */
    @Override
    public boolean hasExtensionMarker() {
        return hasExtensionMarker;
    }

    public boolean get(int bitIndex) {
        return bits.get(bitIndex);
    }

    public void set(int bitIndex, boolean value) {
        bits.set(bitIndex, value);
    }

    public String binaryString() {
        // Write extension bits if the number of named bits is larger than the "size" and
        // those bits are set
        final int sizeWithExtensions = sizeWithExtensions();

        char[] chars = new char[sizeWithExtensions];
        for (int i = 0; i < sizeWithExtensions; i++) {
            chars[i] = get(i) ? '1' : '0';
        }
        return new String(chars);
    }

    // Get the effective bitstring size including extension bits with defined names that are set
    private int sizeWithExtensions() {
        final int numExtensions = names.length - size;  // Number of extensions defined
        int maxSetExtensionIndex = -1;
        for (int extNum = 0; extNum < numExtensions; extNum++) {
            final int extIndex = size + extNum;
            if (get(extIndex)) {
                maxSetExtensionIndex = extIndex;
            }
        }
        return (maxSetExtensionIndex > -1) ? maxSetExtensionIndex + 1 : size;
    }

    public String hexString() {
        final int sizeWithExtensions = sizeWithExtensions();
        HexFormat hex = HexFormat.of().withUpperCase();
        int expectedNumBytes = (sizeWithExtensions + 7) / 8;
        byte[] bytes = reverseBits(bits.toByteArray());
        if (bytes.length < expectedNumBytes) {
            // Pad with 0's to get expected number of bytes
            byte[] paddedBytes = new byte[expectedNumBytes];
            System.arraycopy(bytes, 0, paddedBytes, 0, bytes.length);
            bytes = paddedBytes;
        }
        return hex.formatHex(bytes);
    }

    public void fromBinaryString(String str) {
        if (str == null) {
            bits.clear();
            return;
        }
        char[] chars = str.trim().toCharArray();
        if (chars.length < size) {
            throw new IllegalArgumentException("Not enough characters in string " + str);
        }

        for (int i = 0; i < size; i++) {
            char c = chars[i];
            set(i, c == '1');
        }

        // Read extension bits if the number of named bits is larger than the "size" and the binary
        // string is long enough to contain extensions
        final int numExtensions = names.length - size;  // Number of extensions defined
        final int numExtensionsInString = chars.length - size; // Number of extensions present in string
        if (numExtensions > 0 && numExtensionsInString >= numExtensions) {
             for (int extNum = 0; extNum < numExtensionsInString; extNum++) {
                 final int extIndex = size + extNum;
                 char c = chars[extIndex];
                 set(extIndex, c == '1');
             }
        }
    }

    public void fromHexString(String str) {
        if (str == null) {
            bits.clear();
            return;
        }
        HexFormat hex = HexFormat.of();
        byte[] bytes = reverseBits(hex.parseHex(str));
        BitSet newBits = BitSet.valueOf(bytes);
        bits.clear();
        bits.or(newBits);
    }

    public String name(int index) {
        if (index < 0 || index >= size()) {
            throw new IllegalArgumentException(String.format("Index %s out of range %s-%s", index, 0, size()));
        }
        return names[index];
    }

    @Override
    public int hashCode() {
        return bits.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Asn1Bitstring bitstring) {
            return bits.equals(bitstring.bits);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return binaryString();
    }


}
