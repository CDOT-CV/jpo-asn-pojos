package us.dot.its.jpo.asn.runtime.serialization;

import static us.dot.its.jpo.asn.runtime.annotations.Asn1ParameterizedTypes.IdType.INTEGER;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.TreeNode;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.dataformat.xml.XmlMapper;
import tools.jackson.dataformat.xml.deser.FromXmlParser;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.asn.runtime.annotations.Asn1ParameterizedTypes;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;


/**
 * Deserialize a parameterized SEQUENCE type.
 * Determines the subtype to deserialize to using the {@link Asn1ParameterizedTypes} annotation that
 * must be present.
 *
 * @param <T> The Sequence Type
 *
 * @author Ivan Yourshaw
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@Slf4j
public abstract class ParameterizedTypeDeserializer<T extends Asn1Sequence> extends StdDeserializer<T> {

    protected final Class<T> thisClass;

    protected ParameterizedTypeDeserializer(Class<T> vc) {
        super(vc);
        thisClass = vc;
    }

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws JacksonException {
        final var typeAnnot = thisClass.getAnnotation(Asn1ParameterizedTypes.class);
        if (typeAnnot == null) {
            throw new RuntimeException("Missing Asn1ParameterizedTypes annotation.");
        }
        final String idPropName = typeAnnot.idProperty();
        log.trace("idPropName: {}", idPropName);
        final Asn1ParameterizedTypes.IdType idType = typeAnnot.idType();
        log.trace("idType: {}", idType);
        final Asn1ParameterizedTypes.Type[] types = typeAnnot.value();
        if (types == null || types.length == 0) {
            throw new RuntimeException("No Types are defined in the Asn1ParameterizedTypes annotation.");
        } else {
            for (var t : types) {
                log.trace("type: {}", t);
            }
        }
        if (jsonParser instanceof FromXmlParser xmlParser) {
            // XER
            XmlMapper xmlMapper = (XmlMapper)xmlParser.objectReadContext();
            TreeNode node = xmlMapper.readTree(xmlParser);

            if (node instanceof ObjectNode objectNode) {
                log.trace("ObjectNode: {}", objectNode);
                JsonNode idPropNode = objectNode.findValue(idPropName);
                String xml = xmlMapper.writeValueAsString(node);
                log.trace("node xml: {}", xml);
                if (idPropNode == null) {
                    throw new RuntimeException("idPropNode is null");
                }
                final Object id = (idType == INTEGER) ? idPropNode.asInt() : idPropNode.asString();
                log.trace("id: {}", id);
                Class<?> subType = getSubtypeForId(id, idType, types);
                log.trace("subtype: {}", subType.getName());
                return (T)xmlMapper.readValue(xml, subType);
            } else {
                throw new RuntimeException("Not instance of object");
            }
        } else {
            // JER
            TreeNode node = jsonParser.objectReadContext().readTree(jsonParser);
            var mapper = (ObjectMapper)jsonParser.objectReadContext();
            if (node instanceof ObjectNode objectNode) {
                log.trace("ObjectNode: {}", objectNode);
                JsonNode idPropNode = objectNode.findValue(idPropName);
                String json = mapper.writeValueAsString(objectNode);
                log.trace("node json: {}", json);
                if (idPropNode == null) {
                    throw new RuntimeException("idPropNode is null");
                }
                final Object id = (idType == INTEGER) ? idPropNode.asInt() : idPropNode.asString();
                log.trace("id: {}", id);
                Class<?> subType = getSubtypeForId(id, idType, types);
                log.trace("subtype: {}", subType.getName());
                T deserializedItem = (T)mapper.readValue(json, subType);
                log.trace("deserializedItem: {}", deserializedItem);
                return deserializedItem;
            } else {
                throw new RuntimeException("Not instance of object");
            }
        }
    }

    private Class<?> getSubtypeForId(final Object id, Asn1ParameterizedTypes.IdType idType, Asn1ParameterizedTypes.Type[] types) {
        for (var theType : types) {
            Object idValue = (idType == INTEGER) ? theType.intId() : theType.stringId();
            if (id.equals(idValue)) {
                return theType.value();
            }
        }
        throw new RuntimeException(String.format("Id %s not found in list of types", id));
    }
}
