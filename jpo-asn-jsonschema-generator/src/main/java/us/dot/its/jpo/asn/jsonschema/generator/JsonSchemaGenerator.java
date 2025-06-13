package us.dot.its.jpo.asn.jsonschema.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.Option;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.Configuration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Getter
@Slf4j
public class JsonSchemaGenerator {

  private final Class<?> clazz;
  private final static ObjectMapper mapper = new ObjectMapper();

  public JsonSchemaGenerator(Class<?> clazz) {
    this.clazz = clazz;
  }

  public String generate() throws JsonProcessingException {
    var config = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_7, OptionPreset.PLAIN_JSON)
        .with(Option.EXTRA_OPEN_API_FORMAT_VALUES)
        .without(Option.FLATTENED_ENUMS_FROM_TOSTRING)
        .with(Option.DEFINITIONS_FOR_ALL_OBJECTS)
        .with(new JacksonModule())
        .with(new Asn1Module())
        .build();

    var schemaGenerator = new SchemaGenerator(config);
    ObjectNode schema = schemaGenerator.generateSchema(clazz);
    
    // Create a deep copy of the schema for processing
    ObjectNode schemaCopy = schema.deepCopy();
    
    // Convert to string for JsonPath processing
    String schemaJson = mapper.writeValueAsString(schemaCopy);
    
    // Configure JsonPath to handle missing paths
    Configuration jsonPathConfig = Configuration.builder()
        .build();
    
    try {
      // Find all definitions in the schema
      Object definitions = JsonPath.using(jsonPathConfig)
          .parse(schemaJson)
          .read("$..definitions");
      
      // Only process definitions if we found any and they're not just at the root
      if (definitions != null && !(definitions instanceof Map)) {
        // Get the root definitions
        final ObjectNode rootDefinitions = schema.has("definitions") ? 
            (ObjectNode) schema.get("definitions") : 
            schema.putObject("definitions");
        
        // If definitions is a list (multiple definitions found)
        if (definitions instanceof List) {
          List<Map<String, Object>> defList = (List<Map<String, Object>>) definitions;
          for (Map<String, Object> def : defList) {
            if (def != null) {
              // Merge each definition into root, checking for duplicates
              def.forEach((key, value) -> {
                if (!rootDefinitions.has(key)) {
                  rootDefinitions.set(key, mapper.valueToTree(value));
                }
              });
            }
          }
        }
        
        // Only attempt to remove nested definitions if we have a valid schema
        if (schemaJson != null) {
          // Remove all nested definitions
          String schemaWithoutDefs = JsonPath.using(jsonPathConfig)
              .parse(schemaJson)
              .delete("$..definitions")
              .jsonString();
          
          // Parse back to ObjectNode
          schema = (ObjectNode) mapper.readTree(schemaWithoutDefs);
          
          // Add back the root definitions
          schema.set("definitions", rootDefinitions);
        }
      }
    } catch (PathNotFoundException e) {
      log.debug("No definitions for class: {}", clazz.getName());
    }
    
    var writer = mapper.writerWithDefaultPrettyPrinter();
    return writer.writeValueAsString(schema);
  }
}
