package us.dot.its.jpo.asn.runtime.types;

/**
 * Represents an ASN.1 SEQUENCE type in Java.
 * <p>
 * In ASN.1 notation, a SEQUENCE represents an ordered collection of named elements that may have
 * different types, similar to a struct, record, or class in other programming languages. SEQUENCEs
 * are fundamental building blocks for structured data in ASN.1.
 * <p>
 * Classes implementing this interface represent structured data objects where:
 * <ul>
 *   <li>Elements appear in a specific, predefined order</li>
 *   <li>Each element has a name (field identifier) and type</li>
 *   <li>Elements may be of different types</li>
 *   <li>The structure is statically defined (except for optional elements)</li>
 * </ul>
 *
 * @see Asn1Type The base interface for all ASN.1 types
 * @since 1.0
 */
public abstract class Asn1Sequence implements Asn1Type {

}
