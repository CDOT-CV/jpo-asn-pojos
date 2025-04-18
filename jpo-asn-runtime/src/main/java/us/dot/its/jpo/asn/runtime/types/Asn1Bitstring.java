package us.dot.its.jpo.asn.runtime.types;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import us.dot.its.jpo.asn.runtime.serialization.BitstringSerializer;

import java.util.BitSet;
import java.util.HexFormat;
import java.util.Objects;

import static us.dot.its.jpo.asn.runtime.utils.BitUtils.reverseBits;

/**
 * Base class for ASN.1 BIT STRING types.
 * In ASN.1, a BIT STRING is an ordered sequence of zero or more bits, where each
 * bit may be 0 or 1. This implementation supports:
 * - Named bits
 * - Size constraints (fixed or range)
 * - Extension markers for accommodating future extensions
 */
@JsonSerialize(using = BitstringSerializer.class)
public abstract class Asn1Bitstring implements Asn1Type {

    final BitSet bits;

    // Lower bound
    final int size;

    final int upperBound;

    private int actualSize = 0;

    final boolean hasExtensionMarker;
    final String[] names;


    /**
     * Constructs an Asn1Bitstring object with the specified parameters.
     *
     * @param lowerBound         The minimum number of bits in the bit string.
     * @param upperBound         The maximum number of bits in the bit string.
     * @param hasExtensionMarker Indicates whether the bit string supports an extension marker.
     * @param names              An array of names associated with the bits in the bit string.
     */
    public Asn1Bitstring(int lowerBound, int upperBound, boolean hasExtensionMarker, String[] names) {
        this.size = lowerBound;
        this.upperBound = upperBound;
        this.hasExtensionMarker = hasExtensionMarker;
        this.bits = new BitSet(size);
        this.names = names;
    }

    /**
     * Constructs an Asn1Bitstring object with the specified parameters.
     *
     * @param size               The fixed size of the bit string.
     * @param hasExtensionMarker Indicates whether the bit string supports an extension marker.
     * @param names              An array of names associated with the bits in the bit string.
     */
    public Asn1Bitstring(int size, boolean hasExtensionMarker, String[] names) {
        this(size, size, hasExtensionMarker, names);
    }

    public int size() {
        return size;
    }

    public int upperBound() {
        return upperBound;
    }

    public int getActualSize() {
        return actualSize;
    }

    public void setActualSize(int actualSize) {
        this.actualSize = actualSize;
    }

    public boolean noNamedValues() {
        return names.length == 0;
    }

    public boolean variableSize() {
        return size != upperBound || hasExtensionMarker;
    }

    /**
     * Indicates whether the SIZE constraint of this BIT STRING has an extension marker
     *
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

        // Update actual size
        if (variableSize()) {
            if (getActualSize() < bitIndex + 1) {
                setActualSize(bitIndex + 1);
            }
        }
    }

    /**
     * Set the corresponding bit from the bitstring based on the name value.
     *
     * @param name The name String value for the corresponding bit in the bitstring.
     * @param value The value for the bit to be set.
     */
    public void set(String name, boolean value) {
        for (int i = 0; i < size; i++) {
            if (name(i).equals(name)) {
                set(i, value);
                return;
            }
        }
        throw new IllegalArgumentException("Unknown name " + name);
    }

    public String binaryString() {

        // Write extension bits if the number of named bits is larger than the "size" and
        // those bits are set
        final int resolvedSize = noNamedValues() ? getActualSize() : sizeWithExtensions();

        char[] chars = new char[resolvedSize];
        for (int i = 0; i < resolvedSize; i++) {
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
        final int resolvedSize = variableSize() ? (noNamedValues() ? getActualSize() : sizeWithExtensions()) : size;
        HexFormat hex = HexFormat.of().withUpperCase();
        int expectedNumBytes = (resolvedSize + 7) / 8;
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
            throw new IllegalArgumentException("String too short: " + str + " (expected " + size + " bits) but got (" + chars.length + " bits)");
        }

        // Read all bits in the string if there are no named bits, or if the size is variable or
        // extensible
        if (noNamedValues() || variableSize()) {
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                set(i, c == '1');
            }
            setActualSize(chars.length);
            return;
        }

        // Otherwise only read named bits and extensions
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
        fromHexString(str, null);
    }

    public void fromHexString(String str, Integer length) {
        if (str == null) {
            bits.clear();
            return;
        }
        HexFormat hex = HexFormat.of();
        byte[] bytes = reverseBits(hex.parseHex(str));
        BitSet newBits = BitSet.valueOf(bytes);
        bits.clear();
        bits.or(newBits);

        // If the size is not fixed, or an extensible marker is present, set the actual size
        if (variableSize()) {
            // A length is specified, use it
            // Length not specified, use the number of bits in the hex
            setActualSize(Objects.requireNonNullElseGet(length, () -> bytes.length * 8));
        }
    }

    /**
     * Get the name representing the requested index.
     *
     * @param index The index value of the bitstring being requested.
     */
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
