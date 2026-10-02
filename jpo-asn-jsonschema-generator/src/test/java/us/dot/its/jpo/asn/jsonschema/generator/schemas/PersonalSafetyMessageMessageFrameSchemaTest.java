package us.dot.its.jpo.asn.jsonschema.generator.schemas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.JsonNode;
import com.networknt.schema.SchemaRegistry;
import com.networknt.schema.SpecificationVersion;
import com.networknt.schema.Schema;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;

public class PersonalSafetyMessageMessageFrameSchemaTest {
  @Test
  void testPersonalSafetyMessageMessageFrameJsonAgainstSchema() throws Exception {
    // Load JSON instance
    String jsonPath = "src/test/resources/us/dot/its/jpo/asn/jsonschema/generator/PersonalSafetyMessage/psm_mf.json";
    String jsonData = new String(Files.readAllBytes(Paths.get(jsonPath)), StandardCharsets.UTF_8);

    // Load JSON schema
    String schemaPath = "src/main/resources/schemas/PersonalSafetyMessage/PersonalSafetyMessageMessageFrame.schema.json";
    String schemaData = new String(Files.readAllBytes(Paths.get(schemaPath)), StandardCharsets.UTF_8);

    // Validate
    JsonMapper mapper = JsonMapper.builder().build();
    JsonNode jsonNode = mapper.readTree(jsonData);
    JsonNode schemaNode = mapper.readTree(schemaData);

    SchemaRegistry registry = SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_7);
    Schema schema = registry.getSchema(schemaNode);

    List<com.networknt.schema.Error> errors = schema.validate(jsonNode);
    assertTrue(errors.isEmpty(), "JSON should be valid against the schema. Errors: " + errors);
  }
}
