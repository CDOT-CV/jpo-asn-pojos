package us.dot.its.jpo.asn.jsonschema.generator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.asn.j2735.r2024.MessageFrame.DSRCmsgID;
import com.networknt.schema.SchemaRegistry;
import com.networknt.schema.SpecificationVersion;
import com.networknt.schema.Schema;
import java.util.List;

@Slf4j
public class JsonSchemaGeneratorTest {

    private final static JsonMapper mapper = JsonMapper.builder().build();

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
                schemaNode.get("$schema").asString(),
                equalTo("http://json-schema.org/draft-07/schema#"));

        assertThat("Schema should be of type object",
                schemaNode.get("type").asString(),
                equalTo("object"));

        assertThat("Schema should have properties or oneOf",
                schemaNode.has("properties") || schemaNode.has("oneOf"),
                is(true));

        String resourceBase = "/us/dot/its/jpo/asn/jsonschema/generator/" + pduName.substring(0, 1).toUpperCase()
                + pduName.substring(1);
        List<String> resources = JsonFileLoader.listAllResourcesInDirectory(resourceBase);
        for (String resource : resources) {
            // Ignore message frame JSON files
            if (resource.contains("message_frame") || resource.contains("mf.json")) {
                continue;
            }
            String json = JsonFileLoader.loadResource(resource);
            JsonNode jsonNode = mapper.readTree(json);
            SchemaRegistry registry = SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_7);
            Schema jsonSchema = registry.getSchema(schemaNode);
            List<com.networknt.schema.Error> errors = jsonSchema.validate(jsonNode);
            assertThat("Sample JSON should be valid against the generated schema: " + resource, errors, empty());
        }
    }

}