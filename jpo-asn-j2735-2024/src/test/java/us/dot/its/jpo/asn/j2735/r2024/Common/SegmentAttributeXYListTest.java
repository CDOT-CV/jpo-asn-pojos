package us.dot.its.jpo.asn.j2735.r2024.Common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the SegmentAttributeXYList class.
 */
public class SegmentAttributeXYListTest {
  @Test
  public void testSerialization() throws JsonProcessingException {
    SegmentAttributeXYList list = new SegmentAttributeXYList();
    list.add(SegmentAttributeXY.ADJACENTBIKELANEONRIGHT);
    list.add(SegmentAttributeXY.CURBONLEFT);

    ObjectMapper xmlMapper = new XmlMapper();
    String xml = xmlMapper.writeValueAsString(list);

    assertNotNull(xml);
    assertTrue(xml.contains("<item><adjacentBikeLaneOnRight/></item>"));
    assertTrue(xml.contains("<item><curbOnLeft/></item>"));
  }

  @Test
  public void testDeserialization() throws JsonProcessingException {
    String xml = "<SegmentAttributeXYList>" +
        "<item><adjacentBikeLaneOnRight/></item>" +
        "<item><curbOnLeft/></item>" +
        "</SegmentAttributeXYList>";

    ObjectMapper xmlMapper = new XmlMapper();
    SegmentAttributeXYList list = xmlMapper.readValue(xml, SegmentAttributeXYList.class);

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
