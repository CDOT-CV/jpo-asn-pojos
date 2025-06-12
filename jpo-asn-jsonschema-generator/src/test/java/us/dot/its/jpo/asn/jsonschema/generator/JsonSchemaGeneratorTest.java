package us.dot.its.jpo.asn.jsonschema.generator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.asn.j2735.r2024.MessageFrame.DSRCmsgID;

@Slf4j
public class JsonSchemaGeneratorTest {

    private final static ObjectMapper mapper = new ObjectMapper();

    private static Stream<Arguments> pduClassProvider() {
        return DSRCmsgID.names().stream()
            .filter(name -> !name.endsWith("-D")) // Filter out deprecated messages
            .map(name -> {
                String className = name.substring(0, 1).toUpperCase() + name.substring(1);
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
    }

    @Test
    void testPrimitiveTypeHandling() throws IOException {
        // Test integer type
        JsonSchemaGenerator intGenerator = new JsonSchemaGenerator(int.class);
        String intSchema = intGenerator.generate();
        JsonNode intSchemaNode = mapper.readTree(intSchema);
        assertThat(intSchemaNode.get("type").asText(), equalTo("integer"));

        // Test boolean type
        JsonSchemaGenerator boolGenerator = new JsonSchemaGenerator(boolean.class);
        String boolSchema = boolGenerator.generate();
        JsonNode boolSchemaNode = mapper.readTree(boolSchema);
        assertThat(boolSchemaNode.get("type").asText(), equalTo("boolean"));
    }
} 