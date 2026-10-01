package us.dot.its.jpo.asn.runtime.serialization;

import java.io.StringWriter;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;
import tools.jackson.dataformat.xml.ser.ToXmlGenerator;
import tools.jackson.dataformat.xml.ser.XmlSerializationContext;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.dataformat.xml.util.XmlRootNameLookup;
import us.dot.its.jpo.asn.runtime.types.Asn1SequenceOf;
import us.dot.its.jpo.asn.runtime.types.Asn1Type;

/**
 * This serializer is needed to work around a couple Jackson issues with serializing collections to
 * XML.
 *
 * <p>One issue happens when serializing a root-level collection to XML. Jackson defaults to
 * naming the items "item" instead of using the root name of the item class, and the
 * {@link tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper} and
 * {@link tools.jackson.dataformat.xml.annotation.JacksonXmlProperty} annotations can't be
 * used at the class level to specify the element name of the items.  Annotate the collection class
 * like this to enable serializing it with the correct item elements.  For example:
 * <pre>
 * {@code
 *    @JsonSerialize(using = RootSequenceOfSerializer.class)
 *    public class ListOfThings extends Asn1SequenceOf<Thing> {...}
 *
 *    public class Thing extends Asn1Sequence {}
 * }
 * </pre>
 *
 * Can be serialized as:
 * <pre>
 *   {@code
 *   <ListOfThings>
 *     <Thing>...<Thing>
 *     <Thing>...<Thing>
 *   <ListOfThings>
 *   }
 * </pre>
 *
 * <p>Without the custom serializer the XML serialization would look like:
 * <pre>
 *   {@code
 *   <ListOfThings>
 *     <item>...</item>
 *     <item>...</item>
 *   </ListOfThings>
 *   }
 * </pre>
 * since it's not possible to add the
 * {@link tools.jackson.dataformat.xml.annotation.JacksonXmlProperty} annotation to the
 * collection class to specify the item name, and any
 * {@link com.fasterxml.jackson.annotation.JsonRootName} annotation added to the "Thing" class is
 * otherwise ignored when serializing a collection containing it.</p>
 *
 * <p>Another issue happens when a class has more than one collection property with the same item
 * type. This is a known Jackson issue documented here:
 * <ul>
 * <li><a href="https://github.com/FasterXML/jackson-dataformat-xml/issues/27">jackson-dataformat-xml: issues #27</a></li>
 * <li><a href="https://github.com/FasterXML/jackson-dataformat-xml/issues/192">jackson-dataformat-xml: issues #192</a></li>
 * </ul>
 * <p>For example, here is a class containing two different types which wrap items of the same type:
 * <pre>
 * {@code
 *  public class Example extends Asn1Sequence {
 *
 *    @JsonProperty("first")
 *    @JacksonXmlElementWrapper(localName = "first")
 *    @JacksonXmlProperty(localName = "Thing")
 *    private ListOfFirstThings first;
 *
 *    @JsonProperty("second")
 *    @JacksonXmlElementWrapper(localName = "second")
 *    @JacksonXmlProperty(localName = "Thing")
 *    private ListOfSecondThings second;
 *
 *    public static class ListOfFirstThings extends Asn1SequenceOf<Thing> {...}
 *
 *    public static class ListOfSecondThings extends Asn1SequenceOf<Thing> {...}
 *
 *    ...
 *   }
 * }
 * </pre>
 * <p>The usual XML wrapper annotations don't work due to the matching localNames of the items
 * causing a Jackson exception. To get this to work, annotate each of the Collection classes with a
 * root name and with this serializer, like this:
 * <pre>
 * {@code
 *   public class Example extends Asn1Sequence {
 *
 *     @JsonProperty("first")
 *     private ListOfFirstThings first;
 *
 *     @JsonProperty("second")
 *     private ListOfSecondThings second;
 *
 *     @JsonSerialize(using = RootSequenceOfSerializer.class)
 *     @JsonRootName("first")
 *     public static class ListOfFirstThings extends Asn1SequenceOf<Thing> {...}
 *
 *     @JsonSerialize(using = RootSequenceOfSerializer.class)
 *     @JsonRootName("second")
 *     public static class ListOfSecondThings extends Asn1SequenceOf<Thing> {...}
 *
 *     ...
 *  }
 * }
 * </pre>
 */
@Slf4j
public class RootSequenceOfSerializer
    extends StdSerializer<Asn1SequenceOf<Asn1Type>> {

  private static final XmlRootNameLookup ROOT_NAME_LOOKUP = new XmlRootNameLookup();

  protected RootSequenceOfSerializer() {
    super(Asn1SequenceOf.class);
  }

  @Override
  public void serialize(Asn1SequenceOf<Asn1Type> sequenceOf, JsonGenerator jsonGenerator,
      SerializationContext serializerProvider) {
    if (serializerProvider instanceof XmlSerializationContext) {
      // XER
      // Workaround for a root level collection.
      var xmlGen = (ToXmlGenerator) jsonGenerator;
      xmlGen.writeStartArray();
      for (Asn1Type item : sequenceOf) {
        var sw = new StringWriter();
        try (ToXmlGenerator itemGen = (ToXmlGenerator)serializerProvider.createGenerator(sw)) {
          itemGen.setNextName(ROOT_NAME_LOOKUP.findRootName(serializerProvider, item.getClass()));
          serializerProvider.writeValue(itemGen, item);
        }
        xmlGen.writeRaw(sw.toString());
      }
      xmlGen.writeEndArray();
    } else {
      // JER
      jsonGenerator.writeStartArray();
      for (Asn1Type item : sequenceOf) {
        jsonGenerator.writePOJO(item);
      }
      jsonGenerator.writeEndArray();
    }
  }

}
