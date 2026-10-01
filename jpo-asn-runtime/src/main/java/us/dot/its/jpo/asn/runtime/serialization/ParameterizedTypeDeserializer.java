package us.dot.its.jpo.asn.runtime.serialization;

import static us.dot.its.jpo.asn.runtime.annotations.Asn1ParameterizedTypes.IdType.INTEGER;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.core.TreeNode;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.bean.BeanDeserializerBase;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.exc.MismatchedInputException;
import tools.jackson.databind.node.ObjectNode;
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
            // XER: SEQUENCE components are in declaration order, so the id element comes first.
            // Read it to choose the subtype, then read the remaining elements directly from the
            // stream into an instance of the subtype.
            JsonToken token = xmlParser.currentToken();
            if (token == JsonToken.START_OBJECT) {
                token = xmlParser.nextToken();
            }
            if (token != JsonToken.PROPERTY_NAME || !idPropName.equals(xmlParser.currentName())) {
                throw MismatchedInputException.from(xmlParser, thisClass,
                    String.format("Expected '%s' as the first element", idPropName));
            }
            xmlParser.nextToken();
            final JsonNode idPropNode = deserializationContext.readTree(xmlParser);
            final Object id = (idType == INTEGER) ? idPropNode.asInt() : idPropNode.asString();
            log.trace("id: {}", id);
            Class<?> subType = getSubtypeForId(id, idType, types);
            log.trace("subtype: {}", subType.getName());
            final JavaType subJavaType = deserializationContext.constructType(subType);
            if (!(deserializationContext.findRootValueDeserializer(subJavaType)
                instanceof BeanDeserializerBase beanDeserializer)) {
                return deserializationContext.reportBadDefinition(subJavaType,
                    "ParameterizedTypeDeserializer requires a bean deserializer for " + subType.getName());
            }
            // The subtype's constructor sets the id
            final Object instance =
                beanDeserializer.getValueInstantiator().createUsingDefault(deserializationContext);
            xmlParser.nextToken();
            return (T) beanDeserializer.deserialize(xmlParser, deserializationContext, instance);
        } else {
            // JER
            TreeNode node = deserializationContext.readTree(jsonParser);
            if (node instanceof ObjectNode objectNode) {
                JsonNode idPropNode = objectNode.findValue(idPropName);
                if (idPropNode == null) {
                    throw new RuntimeException("idPropNode is null");
                }
                final Object id = (idType == INTEGER) ? idPropNode.asInt() : idPropNode.asString();
                log.trace("id: {}", id);
                Class<?> subType = getSubtypeForId(id, idType, types);
                log.trace("subtype: {}", subType.getName());
                return (T)deserializationContext.readTreeAsValue(objectNode, subType);
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
