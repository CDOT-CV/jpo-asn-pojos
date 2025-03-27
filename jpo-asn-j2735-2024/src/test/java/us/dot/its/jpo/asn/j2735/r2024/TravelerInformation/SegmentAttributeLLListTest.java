package us.dot.its.jpo.asn.j2735.r2024.TravelerInformation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the SegmentAttributeLLList class.
 */
public class SegmentAttributeLLListTest {
  @Test
  public void testSerialization() throws JsonProcessingException {
    SegmentAttributeLLList list = new SegmentAttributeLLList();
    list.add(SegmentAttributeLL.ADJACENTBIKELANEONRIGHT);
    list.add(SegmentAttributeLL.CURBONLEFT);

    ObjectMapper xmlMapper = new XmlMapper();
    String xml = xmlMapper.writeValueAsString(list);

    assertNotNull(xml);
    assertTrue(xml.contains("<item><adjacentBikeLaneOnRight/></item>"));
    assertTrue(xml.contains("<item><curbOnLeft/></item>"));
  }

  @Test
  public void testDeserialization() throws JsonProcessingException {
    String xml = "<SegmentAttributeLLList>" +
        "<item><adjacentBikeLaneOnRight/></item>" +
        "<item><curbOnLeft/></item>" +
        "</SegmentAttributeLLList>";

    ObjectMapper xmlMapper = new XmlMapper();
    SegmentAttributeLLList list = xmlMapper.readValue(xml, SegmentAttributeLLList.class);

    assertNotNull(list);
    assertEquals(2, list.size());
    assertEquals(SegmentAttributeLL.ADJACENTBIKELANEONRIGHT, list.get(0));
    assertEquals(SegmentAttributeLL.CURBONLEFT, list.get(1));
  }

  @Test
  public void testListEnumValues() {
    SegmentAttributeLLList.SegmentAttributeLLListDeserializer deserializer = new SegmentAttributeLLList.SegmentAttributeLLListDeserializer();
    SegmentAttributeLL[] values = deserializer.listEnumValues();

    assertNotNull(values);
    assertEquals(SegmentAttributeLL.values().length, values.length);
    for (SegmentAttributeLL value : SegmentAttributeLL.values()) {
      assertTrue(java.util.Arrays.asList(values).contains(value));
    }
  }
}
