package us.dot.its.jpo.asn.runtime;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
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


@Slf4j
public abstract class BaseSerializeTest<T> {

  private final static XmlMapper xmlMapper = XmlMapper.builder().disable(
      MapperFeature.SORT_PROPERTIES_ALPHABETICALLY).build();

  private final static ObjectMapper jsonMapper = JsonMapper.builder().disable(
      MapperFeature.SORT_PROPERTIES_ALPHABETICALLY).build();

  private final Class<T> clazz;

  public BaseSerializeTest(Class<T> clazz) {
    this.clazz = clazz;
  }

  protected String toXml(T object) throws JacksonException {
    String str = xmlMapper.writeValueAsString(object);
    log.debug(str);
    return str;
  }

  protected String toJson(T object) throws JacksonException {
    String str = jsonMapper.writeValueAsString(object);
    log.debug(str);
    return str;
  }

  protected T fromXml(String xml) throws IOException {
    T object = xmlMapper.readValue(xml, clazz);
    if (object != null) {
      log.debug(object.toString());
    }
    return object;
  }

  protected T fromJson(String json) throws IOException {
    T object = jsonMapper.readValue(json, clazz);
    if (object != null) {
      log.debug(object.toString());
    }
    return object;
  }

  protected String loadResource(String path) throws IOException {
    String str;
    str = IOUtils.resourceToString(path, StandardCharsets.UTF_8);
    log.debug("Loaded resource: {}", str);
    return str;
  }

  protected static List<String> listAllResourcesInDirectory(String directory)
      throws IOException, URISyntaxException {
    List<String> resources = new ArrayList<>();

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

    return resources;
  }

  protected static Stream<Arguments> getResources(String directory)
      throws IOException, URISyntaxException {
    List<String> resources = listAllResourcesInDirectory(directory);
    var streamBuilder = Stream.<Arguments>builder();
    resources.forEach(resource -> streamBuilder.add(Arguments.of(resource)));
    return streamBuilder.build();
  }

}