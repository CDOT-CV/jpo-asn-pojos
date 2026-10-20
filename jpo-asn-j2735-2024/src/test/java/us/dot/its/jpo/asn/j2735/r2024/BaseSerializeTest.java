package us.dot.its.jpo.asn.j2735.r2024;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.MapperFeature;
import tools.jackson.dataformat.xml.XmlMapper;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.params.provider.Arguments;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
public abstract class BaseSerializeTest<T> {

  public final static XmlMapper XML_MAPPER = XmlMapper.builder()
      .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY).build();

  public final static JsonMapper JSON_MAPPER = JsonMapper.builder()
      .disable(tools.jackson.databind.MapperFeature.SORT_PROPERTIES_ALPHABETICALLY).build();

  private final Class<T> clazz;

  public BaseSerializeTest(Class<T> clazz) {
    this.clazz = clazz;
  }

  protected String toXml(T object) throws JacksonException {
    String str = XML_MAPPER.writeValueAsString(object);
    log.debug(str);
    return str;
  }

  protected String toJson(T object) throws JacksonException {
    String str = JSON_MAPPER.writeValueAsString(object);
    log.debug(str);
    return str;
  }

  protected T fromXml(String xml) throws IOException {
    T object = XML_MAPPER.readValue(xml, clazz);
    log.debug(object.toString());
    return object;
  }

  protected T fromJson(String json) throws IOException {
    T object = JSON_MAPPER.readValue(json, clazz);
    log.debug(object.toString());
    return object;
  }

  protected String loadResource(String path) {
    String str;
    try {
      str = IOUtils.resourceToString(path, StandardCharsets.UTF_8);
      log.debug("Loaded resource: {}", str);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return str;
  }

  protected static List<String> listAllResourcesInDirectory(String directory) {
    List<String> resources = new ArrayList<>();
    try {
      URL dirUrl = IOUtils.resourceToURL(directory);
      File dir = new File(dirUrl.toURI());
      String[] files = dir.list();
      if (files != null) {
        for (String fileName : files) {
          String resourcePath = String.format("%s/%s", directory, fileName);
          log.debug(resourcePath);
          resources.add(resourcePath);
        }
      }
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
    return resources;
  }

  protected static Stream<Arguments> getResources(String directory) {
    List<String> resources = listAllResourcesInDirectory(directory);
    return resources.stream().map(Arguments::of);
  }
  

}