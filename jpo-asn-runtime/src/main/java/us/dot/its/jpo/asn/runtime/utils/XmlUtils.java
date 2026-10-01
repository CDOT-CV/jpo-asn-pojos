package us.dot.its.jpo.asn.runtime.utils;

import tools.jackson.core.JsonToken;
import tools.jackson.databind.exc.MismatchedInputException;
import tools.jackson.dataformat.xml.deser.FromXmlParser;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;

@Slf4j
public class XmlUtils {

  /**
   * Remove the outermost element from an XML string.
   *
   * <p>Assumes a single root element, as produced by a Jackson generator.  A leading XML
   * declaration, processing instructions, and comments are skipped, attributes on the root
   * element are ignored, and a self-closing root element unwraps to an empty string.
   *
   * @param xml - Input xml string
   * @return The unwrapped xml
   * @throws IllegalArgumentException If the XML is not well-formed (i.e. if it doesn't have a
   *                                  single root element).
   */
  public static String unwrap(final String xml) {

    if (xml == null) {
      return null;
    }
    if (xml.isBlank()) {
      return "";
    }
    final String trimmed = skipProlog(xml, xml.trim());

    // Get the start tag of the root element
    if (!trimmed.startsWith("<")) {
      throw notWellFormed(xml);
    }
    final int endStartTag = trimmed.indexOf('>');
    if (endStartTag < 0) {
      throw notWellFormed(xml);
    }
    final String startName = elementName(trimmed);
    log.trace("startName: {}", startName);
    if (startName.isEmpty()) {
      throw notWellFormed(xml);
    }

    // Self-closing root element
    if (trimmed.charAt(endStartTag - 1) == '/') {
      if (endStartTag != trimmed.length() - 1) {
        throw notWellFormed(xml);
      }
      return "";
    }

    // Get the end tag of the root element
    final int startEndTag = trimmed.lastIndexOf("</");
    if (startEndTag <= endStartTag || !trimmed.endsWith(">")) {
      throw notWellFormed(xml);
    }
    final String endName = trimmed.substring(startEndTag + 2, trimmed.length() - 1).trim();
    log.trace("endName: {}", endName);
    if (!startName.equals(endName)) {
      throw notWellFormed(xml);
    }

    final String unwrapped = trimmed.substring(endStartTag + 1, startEndTag);
    log.trace("unwrapped: {}", unwrapped);
    return unwrapped;
  }

  // Skip any XML declaration, processing instructions, and comments before the root element
  private static String skipProlog(final String xml, String trimmed) {
    while (true) {
      final String terminator;
      if (trimmed.startsWith("<?")) {
        terminator = "?>";
      } else if (trimmed.startsWith("<!--")) {
        terminator = "-->";
      } else {
        return trimmed;
      }
      final int end = trimmed.indexOf(terminator);
      if (end < 0) {
        throw notWellFormed(xml);
      }
      trimmed = trimmed.substring(end + terminator.length()).trim();
    }
  }

  // Name of the element whose start tag begins the string, ignoring any attributes
  private static String elementName(final String xml) {
    int end = 1;
    while (end < xml.length()) {
      final char c = xml.charAt(end);
      if (Character.isWhitespace(c) || c == '/' || c == '>') {
        break;
      }
      end++;
    }
    return xml.substring(1, end);
  }

  private static IllegalArgumentException notWellFormed(final String xml) {
    return new IllegalArgumentException(String.format("unwrap: XML is not well formed: %s", xml));
  }

  /*
   * The extract methods below rebuild XML text from the FromXmlParser token stream, preserving
   * the document order of all elements, including repeated elements, which reading into a
   * JsonNode tree does not.
   *
   * They are called from within a custom deserialize method with the parser positioned on the
   * START_OBJECT (or first PROPERTY_NAME) of the current element.  They return with the parser
   * positioned on that element's matching END_OBJECT, as required of a custom deserializer.  If
   * the current element is empty, the parser is on a scalar token instead, and is not advanced.
   */

  /**
   * Extract the child elements of the current XML element, in document order with duplicates.
   *
   * @param xmlParser - The XML Parser from within a custom
   *                  {@link tools.jackson.databind.deser.std.StdDeserializer#deserialize}
   *                  method.
   * @return List of XML strings, one per child element.  Empty if the current element is empty.
   */
  public static List<String> extractXmlList(FromXmlParser xmlParser) {
    if (!isObjectStart(xmlParser.currentToken())) {
      return new ArrayList<>();
    }
    return readChildren(xmlParser);
  }

  /**
   * Extract the single child element of the current XML element, for example, the content of an
   * open type wrapper element.
   *
   * @param xmlParser - The XML Parser from within a custom
   *                  {@link tools.jackson.databind.deser.std.StdDeserializer#deserialize}
   *                  method.
   * @return The XML of the child element.
   * @throws MismatchedInputException If the current element does not have exactly one child.
   */
  public static String extractXmlElement(FromXmlParser xmlParser) {
    final List<String> children = extractXmlList(xmlParser);
    if (children.size() != 1) {
      throw MismatchedInputException.from(xmlParser, (Class<?>) null,
          String.format("Expected exactly one child element, found %d", children.size()));
    }
    return children.get(0);
  }

  /**
   * Extract the complete current XML element.
   *
   * @param xmlParser - The XML Parser from within a custom
   *                  {@link tools.jackson.databind.deser.std.StdDeserializer#deserialize}
   *                  method.
   * @param rootName  - Name of the root element to wrap the extracted content in.  Jackson ignores
   *                  the root element name when reading, so any valid name works.
   * @return The reconstructed XML, wrapped in a root element named {@code rootName}.
   */
  public static String extractXmlObject(FromXmlParser xmlParser, String rootName) {
    final JsonToken token = xmlParser.currentToken();
    final String content;
    if (isObjectStart(token)) {
      content = String.join("", readChildren(xmlParser));
    } else if (token != null && token.isScalarValue() && token != JsonToken.VALUE_NULL) {
      content = StringEscapeUtils.escapeXml11(xmlParser.getString());
    } else {
      content = "";
    }
    return String.format("<%s>%s</%s>", rootName, content, rootName);
  }

  private static boolean isObjectStart(JsonToken token) {
    return token == JsonToken.START_OBJECT || token == JsonToken.PROPERTY_NAME;
  }

  // Parser on START_OBJECT or the first PROPERTY_NAME, leaves it on the matching END_OBJECT
  private static List<String> readChildren(FromXmlParser xmlParser) {
    final List<String> children = new ArrayList<>();
    JsonToken token = xmlParser.currentToken();
    if (token == JsonToken.START_OBJECT) {
      token = nextToken(xmlParser);
    }
    while (token != JsonToken.END_OBJECT) {
      if (token != JsonToken.PROPERTY_NAME) {
        throw MismatchedInputException.from(xmlParser, (Class<?>) null,
            String.format("Expected an XML element, found %s", token));
      }
      children.add(readElement(xmlParser, xmlParser.currentName()));
      token = nextToken(xmlParser);
    }
    return children;
  }

  // Parser on the element's PROPERTY_NAME, leaves it on the last token of the element's value
  private static String readElement(FromXmlParser xmlParser, String name) {
    final JsonToken token = nextToken(xmlParser);
    if (token == JsonToken.START_OBJECT) {
      return String.format("<%s>%s</%s>", name, String.join("", readChildren(xmlParser)), name);
    } else if (token == JsonToken.VALUE_NULL) {
      return String.format("<%s/>", name);
    } else if (token.isScalarValue()) {
      return String.format("<%s>%s</%s>", name,
          StringEscapeUtils.escapeXml11(xmlParser.getString()), name);
    }
    throw MismatchedInputException.from(xmlParser, (Class<?>) null,
        String.format("Unexpected token %s in XML element <%s>", token, name));
  }

  private static JsonToken nextToken(FromXmlParser xmlParser) {
    final JsonToken token = xmlParser.nextToken();
    if (token == null) {
      throw MismatchedInputException.from(xmlParser, (Class<?>) null,
          "Unexpected end of input while extracting XML");
    }
    return token;
  }
}
