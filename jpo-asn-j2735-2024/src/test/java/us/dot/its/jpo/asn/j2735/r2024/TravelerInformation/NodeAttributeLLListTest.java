package us.dot.its.jpo.asn.j2735.r2024.TravelerInformation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the NodeAttributeLLList class.
 */
public class NodeAttributeLLListTest {
  @Test
  public void testSerialization() throws JsonProcessingException {
    NodeAttributeLLList list = new NodeAttributeLLList();
    list.add(NodeAttributeLL.ROUNDEDCAPSTYLEB);
    list.add(NodeAttributeLL.DOWNSTREAMSTOPLINE);

    ObjectMapper xmlMapper = new XmlMapper();
    String xml = xmlMapper.writeValueAsString(list);

    assertNotNull(xml);
    assertTrue(xml.contains("<item><roundedCapStyleB/></item>"));
    assertTrue(xml.contains("<item><downstreamStopLine/></item>"));
  }

  @Test
  public void testDeserialization() throws JsonProcessingException {
    String xml = "<NodeAttributeLLList>" +
        "<item><roundedCapStyleB/></item>" +
        "<item><downstreamStopLine/></item>" +
        "</NodeAttributeLLList>";

    ObjectMapper xmlMapper = new XmlMapper();
    NodeAttributeLLList list = xmlMapper.readValue(xml, NodeAttributeLLList.class);

    assertNotNull(list);
    assertEquals(2, list.size());
    assertEquals(NodeAttributeLL.ROUNDEDCAPSTYLEB, list.get(0));
    assertEquals(NodeAttributeLL.DOWNSTREAMSTOPLINE, list.get(1));
  }

  @Test
  public void testListEnumValues() {
    NodeAttributeLLList.NodeAttributeLLListDeserializer deserializer = new NodeAttributeLLList.NodeAttributeLLListDeserializer();
    NodeAttributeLL[] values = deserializer.listEnumValues();

    assertNotNull(values);
    assertEquals(NodeAttributeLL.values().length, values.length);
    for (NodeAttributeLL value : NodeAttributeLL.values()) {
      assertTrue(java.util.Arrays.asList(values).contains(value));
    }
  }
}
