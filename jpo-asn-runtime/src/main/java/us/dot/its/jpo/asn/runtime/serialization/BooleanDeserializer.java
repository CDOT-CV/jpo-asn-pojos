package us.dot.its.jpo.asn.runtime.serialization;

import tools.jackson.core.JsonParser;
import tools.jackson.core.TreeNode;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.exc.ValueInstantiationException;
import tools.jackson.dataformat.xml.deser.FromXmlParser;
import us.dot.its.jpo.asn.runtime.types.Asn1Boolean;

import java.io.IOException;
import java.lang.reflect.Constructor;

@SuppressWarnings({"unchecked"})
public final class BooleanDeserializer<T extends Asn1Boolean> extends StdDeserializer<T> {

    private final Class<T> valueType;

    public BooleanDeserializer() {
        super(Asn1Boolean.class);
        this.valueType = (Class<T>) Asn1Boolean.class;
    }

    public BooleanDeserializer(Class<T> valueType) {
        super(valueType);
        this.valueType = valueType;
    }

    private T construct(JsonParser jsonParser) throws ValueInstantiationException {
        try {
            if (valueType == Asn1Boolean.class) {
                return (T) new Asn1Boolean();
            }
            Constructor<T> constructor = valueType.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw ValueInstantiationException.from(jsonParser,
                "Failed to create instance of " + valueType.getName(), getValueType(), e);
        }
    }

    @Override
    public ValueDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        JavaType type;
        if (property != null) {
            type = property.getType();
        } else {
            type = ctxt.getContextualType();
        }
        if (type.isTypeOrSubTypeOf(Asn1Boolean.class)) {
            return new BooleanDeserializer<>((Class<T>) type.getRawClass());
        }
        return this;
    }

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        T result = construct(jsonParser);
        if (jsonParser instanceof FromXmlParser) {
            // XML: unwrap empty element
            TreeNode node = jsonParser.objectReadContext().readTree(jsonParser);
            var propNames = node.propertyNames();
            for (var propName : propNames) {
                result.setValue(Boolean.parseBoolean(propName));
            }
        } else {
            // JSON
            result.setValue(jsonParser.getBooleanValue());
        }
        return result;
    }
}
