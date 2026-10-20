package us.dot.its.jpo.asn.j2735.r2024.Common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static us.dot.its.jpo.asn.j2735.r2024.BaseSerializeTest.XML_MAPPER;

import org.junit.jupiter.api.Test;
import tools.jackson.core.JacksonException;

/**
 * Unit tests for the NodeAttributeXYList class.
 */
public class NodeAttributeXYListTest {
  @Test
  public void testSerialization() throws JacksonException {
    NodeAttributeXYList list = new NodeAttributeXYList();
    list.add(NodeAttributeXY.ROUNDEDCAPSTYLEB);
    list.add(NodeAttributeXY.DOWNSTREAMSTOPLINE);

    String xml = XML_MAPPER.writeValueAsString(list);

    assertNotNull(xml);
    assertTrue(xml.contains("<item><roundedCapStyleB/></item>"));
    assertTrue(xml.contains("<item><downstreamStopLine/></item>"));
  }

  @Test
  public void testDeserialization() throws JacksonException {
    String xml = "<NodeAttributeXYList>" +
        "<item><roundedCapStyleB/></item>" +
        "<item><downstreamStopLine/></item>" +
        "</NodeAttributeXYList>";

    NodeAttributeXYList list = XML_MAPPER.readValue(xml, NodeAttributeXYList.class);

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
