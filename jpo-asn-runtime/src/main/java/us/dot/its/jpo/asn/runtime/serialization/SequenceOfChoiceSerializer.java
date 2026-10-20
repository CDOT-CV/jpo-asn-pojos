package us.dot.its.jpo.asn.runtime.serialization;

import java.io.StringWriter;
import javax.xml.namespace.QName;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;
import tools.jackson.dataformat.xml.ser.ToXmlGenerator;
import tools.jackson.dataformat.xml.ser.XmlSerializationContext;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.asn.runtime.types.Asn1Choice;
import us.dot.its.jpo.asn.runtime.types.Asn1SequenceOf;

/**
 * Serializer for SEQUENCE-OF CHOICE types. These are unwrapped in XER, but wrapped in JER.
 *
 * @param <S> The Asn1Choice type
 * @param <T> The Asn1SequenceOf type
 */
@Slf4j
public class SequenceOfChoiceSerializer<S extends Asn1Choice, T extends Asn1SequenceOf<S>>
    extends StdSerializer<T> {

  protected final Class<S> choiceClass;
  protected final Class<T> sequenceOfClass;

  private static final String ITEM = "item";
  private static final String ITEM_START = "<" + ITEM + ">";
  private static final String ITEM_END = "</" + ITEM + ">";
  private static final String ITEM_EMPTY = "<" + ITEM + "/>";

  protected SequenceOfChoiceSerializer(Class<S> choiceClass, Class<T> sequenceOfClass) {
    super(sequenceOfClass);
    this.choiceClass = choiceClass;
    this.sequenceOfClass = sequenceOfClass;
  }

  @Override
  public void serialize(T sequenceOf, JsonGenerator jsonGenerator,
      SerializationContext serializerProvider) {
    if (serializerProvider instanceof XmlSerializationContext) {
      // XER: Choice items not wrapped
      var xmlGen = (ToXmlGenerator) jsonGenerator;

      xmlGen.writeStartArray();
      for (S choiceItem : sequenceOf) {
        log.trace(
            "SequenceOfChoiceSerializer: ChoiceClass: {}, SequenceOfClass: {}, choiceItem: {}",
            choiceClass.getName(), sequenceOfClass.getName(),
            choiceItem);
        var sw = new StringWriter();
        try (ToXmlGenerator xGen = (ToXmlGenerator)serializerProvider.createGenerator(sw)) {
          xGen.setNextName(new QName(ITEM));
          serializerProvider.writeValue(xGen, choiceItem);
        }
        xmlGen.writeRaw(stripItemElement(sw.toString()));
      }
      xmlGen.writeEndArray();

    } else {
      // JER: Normal, choice items are wrapped
      jsonGenerator.writePOJO(sequenceOf);
    }
  }

  // Remove the placeholder root element each choice item is written under, leaving the
  // element of the chosen alternative
  private static String stripItemElement(String xml) {
    final String trimmed = xml.trim();
    if (trimmed.equals(ITEM_EMPTY)) {
      return "";
    }
    if (!trimmed.startsWith(ITEM_START) || !trimmed.endsWith(ITEM_END)
        || trimmed.length() < ITEM_START.length() + ITEM_END.length()) {
      throw new IllegalStateException("Unexpected choice item XML: " + xml);
    }
    return trimmed.substring(ITEM_START.length(), trimmed.length() - ITEM_END.length());
  }

}
