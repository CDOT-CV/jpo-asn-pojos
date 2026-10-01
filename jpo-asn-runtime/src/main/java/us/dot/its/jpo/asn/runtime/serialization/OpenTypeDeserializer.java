package us.dot.its.jpo.asn.runtime.serialization;

import static us.dot.its.jpo.asn.runtime.utils.XmlUtils.extractXmlElement;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.core.TreeNode;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.dataformat.xml.XmlMapper;
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
    private static final XmlMapper XML_MAPPER = XmlMapper.builder()
        .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY).build();

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
            // XML: Unwrap
            log.debug("deserialize open type: xml");
            final int depth = xmlParser.streamReadContext().getNestingDepth();
            String xml = extractXmlElement(xmlParser);
            // Leave the parser on the END_OBJECT of the open type wrapper element
            while (!(xmlParser.currentToken() == JsonToken.END_OBJECT
                && xmlParser.streamReadContext().getNestingDepth() < depth)) {
                xmlParser.nextToken();
            }
            log.debug("extracted xml: {}", xml);
            result = XML_MAPPER.readValue(xml, thisClass);
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
