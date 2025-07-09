package us.dot.its.jpo.asn.runtime.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class SerializationUtils {

  public static final XmlMapper xmlMapper = new XmlMapper();
  public static final ObjectMapper objectMapper = new ObjectMapper();

}
