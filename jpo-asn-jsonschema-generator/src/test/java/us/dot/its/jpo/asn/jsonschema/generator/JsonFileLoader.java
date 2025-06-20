package us.dot.its.jpo.asn.jsonschema.generator;

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
public class JsonFileLoader {

  public static String loadResource(String path) {
    String str;
    try {
      str = IOUtils.resourceToString(path, StandardCharsets.UTF_8);
      log.debug("Loaded resource: {}", str);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return str;
  }

  public static List<String> listAllResourcesInDirectory(String directory) {
    List<String> resources = new ArrayList<>();
    try {
      URL dirUrl = IOUtils.resourceToURL(directory);
      File dir = new File(dirUrl.toURI());
      if (!dir.exists() || !dir.isDirectory() || dir.toString().contains("bin")) {
        log.warn("Resource directory does not exist or is not a directory: {}", directory);
        return resources;
      }
      addResourcesRecursively(dir, directory, resources);
    } catch (IOException | URISyntaxException e) {
      log.warn("Exception while listing resources in directory: {}: {}", directory, e.getMessage());
      // Gracefully return empty list
      return resources;
    }
    return resources;
  }

  private static void addResourcesRecursively(File dir, String relativePath, List<String> resources) {
    File[] files = dir.listFiles();
    if (files != null) {
      for (File file : files) {
        String resourcePath = String.format("%s/%s", relativePath, file.getName());
        if (file.isDirectory()) {
          addResourcesRecursively(file, resourcePath, resources);
        } else {
          resources.add(resourcePath);
        }
      }
    }
  }

  public static Stream<Arguments> getResources(String directory) {
    List<String> resources = listAllResourcesInDirectory(directory);
    return resources.stream().map(Arguments::of);
  }
  

}