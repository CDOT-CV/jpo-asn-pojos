package us.dot.its.jpo.asn.j2735.r2024.Common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the NodeAttributeXYList class.
 */
public class NodeAttributeXYListTest {
  @Test
  public void testSerialization() throws JsonProcessingException {
    NodeAttributeXYList list = new NodeAttributeXYList();
    list.add(NodeAttributeXY.ROUNDEDCAPSTYLEB);
    list.add(NodeAttributeXY.DOWNSTREAMSTOPLINE);

    ObjectMapper xmlMapper = new XmlMapper();
    String xml = xmlMapper.writeValueAsString(list);

    assertNotNull(xml);
    assertTrue(xml.contains("<item><roundedCapStyleB/></item>"));
    assertTrue(xml.contains("<item><downstreamStopLine/></item>"));
  }

  @Test
  public void testDeserialization() throws JsonProcessingException {
    String xml = "<NodeAttributeXYList>" +
        "<item><roundedCapStyleB/></item>" +
        "<item><downstreamStopLine/></item>" +
        "</NodeAttributeXYList>";

    ObjectMapper xmlMapper = new XmlMapper();
    NodeAttributeXYList list = xmlMapper.readValue(xml, NodeAttributeXYList.class);

    assertNotNull(list);
    assertEquals(2, list.size());
    assertEquals(NodeAttributeXY.ROUNDEDCAPSTYLEB, list.get(0));
    assertEquals(NodeAttributeXY.DOWNSTREAMSTOPLINE, list.get(1));
  }

  @Test
  public void testListEnumValues() {
    NodeAttributeXYList.NodeAttributeXYListDeserializer deserializer = new NodeAttributeXYList.NodeAttributeXYListDeserializer();
    NodeAttributeXY[] values = deserializer.listEnumValues();

    assertNotNull(values);
    assertEquals(NodeAttributeXY.values().length, values.length);
    for (NodeAttributeXY value : NodeAttributeXY.values()) {
      assertTrue(java.util.Arrays.asList(values).contains(value));
    }
  }
}
