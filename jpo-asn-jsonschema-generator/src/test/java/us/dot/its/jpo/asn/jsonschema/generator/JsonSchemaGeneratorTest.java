package us.dot.its.jpo.asn.jsonschema.generator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.Test;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.asn.j2735.r2024.MessageFrame.DSRCmsgID;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import java.util.List;

@Slf4j
public class JsonSchemaGeneratorTest {

    private final static ObjectMapper mapper = new ObjectMapper();

    private static Stream<Arguments> pduClassProvider() {
        return DSRCmsgID.names().stream()
            .filter(name -> !name.endsWith("-D")) // Filter out deprecated messages
            .map(name -> {
                // keep this logic for most things but cap NMEA Corrections
                String className = name.substring(0, 1).toUpperCase() + name.substring(1);
                if (name.toLowerCase().equals("nmeacorrections")) {
                    className = "NMEAcorrections";
                } else if (name.toLowerCase().equals("rtcmcorrections")) {
                    className = "RTCMcorrections";
                }
                String fullClassName = String.format("us.dot.its.jpo.asn.j2735.r2024.%s.%s", className, className);
                try {
                    Class<?> pduClass = Class.forName(fullClassName);
                    return Arguments.of(name, pduClass);
                } catch (ClassNotFoundException e) {
                    // Skip if class not found
                    return null;
                }
            })
            .filter(arg -> arg != null);
    }

    @ParameterizedTest
    @MethodSource("pduClassProvider")
    void testPduSchemaGeneration(String pduName, Class<?> pduClass) throws IOException {
        log.info("Testing schema generation for PDU: {}", pduName);
        // Create generator for this specific class
        JsonSchemaGenerator generator = new JsonSchemaGenerator(pduClass);
        
        // Generate schema
        String schema = generator.generate();
        assertNotNull(schema, "Generated schema should not be null");
        
        // Parse and validate schema
        JsonNode schemaNode = mapper.readTree(schema);
        
        // Basic schema validation
        assertThat("Schema should be draft-7", 
            schemaNode.get("$schema").asText(),
            equalTo("http://json-schema.org/draft-07/schema#"));
            
        assertThat("Schema should be of type object", 
            schemaNode.get("type").asText(),
            equalTo("object"));
            
        assertThat("Schema should have properties or oneOf", 
            schemaNode.has("properties") || schemaNode.has("oneOf"),
            is(true));

        
        String resourceBase = "/us/dot/its/jpo/asn/jsonschema/generator/" + pduName.substring(0, 1).toUpperCase() + pduName.substring(1);
        List<String> resources = JsonFileLoader.listAllResourcesInDirectory(resourceBase);
        for (String resource : resources) {
            // skip message frame files
            if (resource.contains("message_frame")) {
                continue;
            }

            String json = JsonFileLoader.loadResource(resource);
            JsonNode jsonNode = mapper.readTree(json);
            JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
            JsonSchema jsonSchema = factory.getSchema(schemaNode);
            Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
            assertThat("Sample JSON should be valid against the generated schema: " + resource, errors, empty());
        }
    }

    @Test
    void testBasicSafetyMessageMessageFrameSchema() throws IOException {
        log.info("Testing BasicSafetyMessageMessageFrame schema validation");
        
        // Load schema from file
        String schemaPath = "/schemas/BasicSafetyMessage/BasicSafetyMessageMessageFrame.schema.json";
        String schema = JsonFileLoader.loadResource(schemaPath);
        assertNotNull(schema, "Schema file should not be null");
        
        // Parse and validate schema
        JsonNode schemaNode = mapper.readTree(schema);
        
        // Basic schema validation
        assertThat("Schema should be draft-7", 
            schemaNode.get("$schema").asText(),
            equalTo("http://json-schema.org/draft-07/schema#"));
            
        assertThat("Schema should be of type object", 
            schemaNode.get("type").asText(),
            equalTo("object"));
            
        // Check for required BasicSafetyMessage property
        assertThat("Schema should have BasicSafetyMessage property", 
            schemaNode.get("properties").has("BasicSafetyMessage"),
            is(true));
            
        assertThat("BasicSafetyMessage should be required", 
            schemaNode.get("required").toString(),
            containsString("BasicSafetyMessage"));
        
        // Test with sample BSM data
        String bsmJson = JsonFileLoader.loadResource("/us/dot/its/jpo/asn/jsonschema/generator/BasicSafetyMessage/bsm_core.json");
        JsonNode bsmNode = mapper.readTree(bsmJson);
        
        // Create a complete message frame JSON
        String messageFrameJson = String.format(
            "{\"BasicSafetyMessage\": %s}", 
            bsmJson
        );
        
        JsonNode messageFrameNode = mapper.readTree(messageFrameJson);
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
        JsonSchema jsonSchema = factory.getSchema(schemaNode);
        Set<ValidationMessage> errors = jsonSchema.validate(messageFrameNode);
        
        assertThat("Message frame JSON should be valid against the schema", errors, empty());
    }
} 