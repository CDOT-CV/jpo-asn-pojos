package us.dot.its.jpo.asn.jsonschema.generator.schemas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class SignalStatusMessageMessageFrameSchemaTest {
  @Test
  void testSignalStatusMessageMessageFrameJsonAgainstSchema() throws Exception {
    // Load JSON instance
    String jsonPath = "src/test/resources/us/dot/its/jpo/asn/jsonschema/generator/SignalStatusMessage/ssm_mf.json";
    String jsonData = new String(Files.readAllBytes(Paths.get(jsonPath)), StandardCharsets.UTF_8);

    // Load JSON schema
    String schemaPath = "src/main/resources/schemas/SignalStatusMessage/SignalStatusMessageMessageFrame.schema.json";
    String schemaData = new String(Files.readAllBytes(Paths.get(schemaPath)), StandardCharsets.UTF_8);

    // Validate
    ObjectMapper mapper = new ObjectMapper();
    JsonNode jsonNode = mapper.readTree(jsonData);
    JsonNode schemaNode = mapper.readTree(schemaData);

    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
    JsonSchema schema = factory.getSchema(schemaNode);

    Set<ValidationMessage> errors = schema.validate(jsonNode);
    assertTrue(errors.isEmpty(), "JSON should be valid against the schema. Errors: " + errors);
  }
}
