package us.dot.its.jpo.asn.runtime.serialization;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.PropertyName;
import tools.jackson.databind.deser.SettableBeanProperty;
import tools.jackson.databind.deser.bean.BeanDeserializerBase;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.exc.MismatchedInputException;
import tools.jackson.dataformat.xml.deser.FromXmlParser;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.asn.runtime.types.Asn1Choice;
import us.dot.its.jpo.asn.runtime.types.Asn1SequenceOf;


/**
 * Deserializer for SEQUENCE-OF CHOICE types. These are unwrapped in XER, but wrapped in JER.
 *
 * @param <S> The Asn1Choice type
 * @param <T> The Asn1SequenceOf type
 */
@Slf4j
public abstract class SequenceOfChoiceDeserializer<S extends Asn1Choice, T extends Asn1SequenceOf<S>>
    extends StdDeserializer<T> {

  protected final Class<S> choiceClass;
  protected final Class<T> sequenceOfClass;

  protected abstract T construct();

  protected SequenceOfChoiceDeserializer(Class<S> choiceClass, Class<T> sequenceOfClass) {
    super(sequenceOfClass);
    this.choiceClass = choiceClass;
    this.sequenceOfClass = sequenceOfClass;
  }


  @Override
  public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws JacksonException {
    T result = construct();
    if (jsonParser instanceof FromXmlParser xmlParser) {

      // XML: expects unwrapped choice items, each an element named for the chosen alternative.
      // Read them directly from the stream, because reading into a tree doesn't preserve the
      // original order of sequence items.
      JsonToken token = xmlParser.currentToken();
      if (token == JsonToken.START_OBJECT) {
        token = xmlParser.nextToken();
      } else if (token != JsonToken.PROPERTY_NAME) {
        // Empty element, no items
        return result;
      }
      final BeanDeserializerBase choiceDeserializer = findChoiceDeserializer(deserializationContext);
      while (token != JsonToken.END_OBJECT) {
        if (token != JsonToken.PROPERTY_NAME) {
          throw MismatchedInputException.from(xmlParser, sequenceOfClass,
              "Expected a choice element, found " + token);
        }
        final String name = xmlParser.currentName();
        log.trace("SequenceOfChoiceDeserializer: name: {}", name);
        final SettableBeanProperty alternative =
            choiceDeserializer.findProperty(PropertyName.construct(name));
        xmlParser.nextToken();
        if (alternative == null) {
          deserializationContext.handleUnknownProperty(xmlParser, choiceDeserializer, choiceClass,
              name);
        } else {
          final Object choice =
              choiceDeserializer.getValueInstantiator().createUsingDefault(deserializationContext);
          alternative.deserializeAndSet(xmlParser, deserializationContext, choice);
          result.add(choiceClass.cast(choice));
        }
        token = xmlParser.nextToken();
      }
    } else {
      // JSON is easier! It expects wrapped choice items, pass through as normal
      result = jsonParser.objectReadContext().readValue(jsonParser, sequenceOfClass);
    }
    return result;
  }

  private BeanDeserializerBase findChoiceDeserializer(DeserializationContext ctxt) {
    final JavaType choiceType = ctxt.constructType(choiceClass);
    if (ctxt.findRootValueDeserializer(choiceType) instanceof BeanDeserializerBase beanDeserializer) {
      return beanDeserializer;
    }
    return ctxt.reportBadDefinition(choiceType,
        "SequenceOfChoiceDeserializer requires a bean deserializer for " + choiceClass.getName());
  }

}
