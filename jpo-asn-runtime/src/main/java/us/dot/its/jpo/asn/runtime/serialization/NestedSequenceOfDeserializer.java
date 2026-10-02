package us.dot.its.jpo.asn.runtime.serialization;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.dataformat.xml.deser.FromXmlParser;
import us.dot.its.jpo.asn.runtime.types.Asn1SequenceOf;

/**
 * Deserializer for nested, anonymous SEQUENCE-OF types.  Handles XER's way of wrapping these.
 * @param <T> The Sequence Of type
 * @author Ivan Yourshaw
 */
public class NestedSequenceOfDeserializer<T extends Asn1SequenceOf<?>> extends StdDeserializer<T> {

    protected final Class<T> thisClass;
    protected final String wrapped;

    protected NestedSequenceOfDeserializer(Class<T> vc, String wrapped) {
        super(vc);
        this.thisClass = vc;
        this.wrapped = wrapped;
    }

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws JacksonException {
        T result = null;
        if (jsonParser instanceof FromXmlParser xmlParser) {
            // For XML, we need to remove the wrapper and distinguish between single items and arrays
            JsonNode node = deserializationContext.readTree(xmlParser);
            if (node instanceof ObjectNode objectNode) {
                JsonNode unwrapped = objectNode.findValue(wrapped);
                if (unwrapped instanceof ArrayNode arrayNode) {
                    result = deserializationContext.readTreeAsValue(arrayNode, thisClass);
                } else if (unwrapped != null) {
                    // Single item not identified as array, so put it in an array
                    ArrayNode arrayNode = deserializationContext.getNodeFactory().arrayNode();
                    arrayNode.add(unwrapped);
                    result = deserializationContext.readTreeAsValue(arrayNode, thisClass);
                }
            }
        }else {
            result = jsonParser.objectReadContext().readValue(jsonParser, thisClass);
        }
        return result;
    }
}
