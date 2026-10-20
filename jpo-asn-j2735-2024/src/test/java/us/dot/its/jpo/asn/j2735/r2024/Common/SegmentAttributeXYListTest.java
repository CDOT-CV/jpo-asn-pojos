package us.dot.its.jpo.asn.j2735.r2024.Common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static us.dot.its.jpo.asn.j2735.r2024.BaseSerializeTest.XML_MAPPER;

import org.junit.jupiter.api.Test;
import tools.jackson.core.JacksonException;

/**
 * Unit tests for the SegmentAttributeXYList class.
 */
public class SegmentAttributeXYListTest {
  @Test
  public void testSerialization() throws JacksonException {
    SegmentAttributeXYList list = new SegmentAttributeXYList();
    list.add(SegmentAttributeXY.ADJACENTBIKELANEONRIGHT);
    list.add(SegmentAttributeXY.CURBONLEFT);

    String xml = XML_MAPPER.writeValueAsString(list);

    assertNotNull(xml);
    assertTrue(xml.contains("<item><adjacentBikeLaneOnRight/></item>"));
    assertTrue(xml.contains("<item><curbOnLeft/></item>"));
  }

  @Test
  public void testDeserialization() throws JacksonException {
    String xml = "<SegmentAttributeXYList>" +
        "<item><adjacentBikeLaneOnRight/></item>" +
        "<item><curbOnLeft/></item>" +
        "</SegmentAttributeXYList>";

    SegmentAttributeXYList list = XML_MAPPER.readValue(xml, SegmentAttributeXYList.class);

    assertNotNull(list);
    assertEquals(2, list.size());
    assertEquals(SegmentAttributeXY.ADJACENTBIKELANEONRIGHT, list.get(0));
    assertEquals(SegmentAttributeXY.CURBONLEFT, list.get(1));
  }

  @Test
  public void testListEnumValues() {
    SegmentAttributeXYList.SegmentAttributeXYListDeserializer deserializer = new SegmentAttributeXYList.SegmentAttributeXYListDeserializer();
    SegmentAttributeXY[] values = deserializer.listEnumValues();

    assertNotNull(values);
    assertEquals(SegmentAttributeXY.values().length, values.length);
    for (SegmentAttributeXY value : SegmentAttributeXY.values()) {
      assertTrue(java.util.Arrays.asList(values).contains(value));
    }
  }
}
