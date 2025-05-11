package us.dot.its.jpo.asn.jsonschema.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.Option;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import lombok.Getter;

@Getter
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
    var writer = mapper.writerWithDefaultPrettyPrinter();
    return writer.writeValueAsString(schema);
  }

}
