package us.dot.its.jpo.asn.runtime.utils;

import lombok.extern.slf4j.Slf4j;

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
}
