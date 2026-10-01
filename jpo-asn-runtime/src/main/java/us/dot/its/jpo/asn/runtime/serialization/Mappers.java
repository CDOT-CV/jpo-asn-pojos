package us.dot.its.jpo.asn.runtime.serialization;

import tools.jackson.databind.MapperFeature;
import tools.jackson.dataformat.xml.XmlMapper;

public class Mappers {
  public static final XmlMapper XML_MAPPER = XmlMapper.builder()
      .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY).build();
}
