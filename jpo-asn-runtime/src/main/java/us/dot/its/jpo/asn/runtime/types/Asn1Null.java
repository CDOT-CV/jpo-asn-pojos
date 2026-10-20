package us.dot.its.jpo.asn.runtime.types;

import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;
import us.dot.its.jpo.asn.runtime.serialization.NullDeserializer;
import us.dot.its.jpo.asn.runtime.serialization.NullSerializer;

/**
 * Represents an ASN.1 NULL Type.  Used to represent a placeholder for message types for future use
 * in J2735 (2024), or as the value of CHOICE items in some specifications.
 */
@JsonSerialize(using = NullSerializer.class)
@JsonDeserialize(using = NullDeserializer.class)
public class Asn1Null implements Asn1Type {
}
