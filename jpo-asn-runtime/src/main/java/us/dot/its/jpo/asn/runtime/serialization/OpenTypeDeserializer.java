package us.dot.its.jpo.asn.runtime.serialization;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.core.TreeNode;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.exc.MismatchedInputException;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.dataformat.xml.deser.FromXmlParser;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.asn.runtime.types.Asn1Type;

/**
 * See description in {@link OpenTypeSerializer}
 * @author Ivan Yourshaw
 */
@Slf4j
public abstract class OpenTypeDeserializer<T extends Asn1Type> extends StdDeserializer<T> {

    protected final Class<T> thisClass;
    protected final String wrapped;

    protected OpenTypeDeserializer(Class<T> vc, String wrapped) {
        super(vc);
        thisClass = vc;
        this.wrapped = wrapped;
    }


    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws JacksonException {
        T result = null;
        log.debug("deserialize open type");
        if (jsonParser instanceof FromXmlParser xmlParser) {
            // XML: Unwrap, reading the single child element directly from the stream
            log.debug("deserialize open type: xml");
            JsonToken token = xmlParser.currentToken();
            if (token == JsonToken.START_OBJECT) {
                token = xmlParser.nextToken();
            }
            if (token != JsonToken.PROPERTY_NAME) {
                throw MismatchedInputException.from(xmlParser, thisClass,
                    "Expected an open type element, found " + token);
            }
            log.debug("open type element: {}", xmlParser.currentName());
            xmlParser.nextToken();
            result = deserializationContext.readValue(xmlParser, thisClass);
            token = xmlParser.nextToken();
            if (token != JsonToken.END_OBJECT) {
                throw MismatchedInputException.from(xmlParser, thisClass,
                    "Expected exactly one open type element, found " + token);
            }
        } else {
            // JSON:
            log.debug("deserialize open type: json");
            TreeNode node = jsonParser.objectReadContext().readTree(jsonParser);
            //var mapper = (ObjectMapper)jsonParser.objectReadContext();
            if (node instanceof ObjectNode objectNode) {
                // Try unwrapping
                JsonNode innerNode = objectNode.findValue(wrapped);
                if (innerNode != null) {
                    log.debug("deserialize open type json unwrapped {}", wrapped);
                }
                JsonNode useNode = innerNode != null ? innerNode : objectNode;
                result = deserializationContext.readTreeAsValue(useNode, thisClass);
            }
        }
        return result;
    }
}
