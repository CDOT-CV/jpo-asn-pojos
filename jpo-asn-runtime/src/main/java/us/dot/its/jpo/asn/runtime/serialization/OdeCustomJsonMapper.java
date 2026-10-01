package us.dot.its.jpo.asn.runtime.serialization;

import lombok.Getter;
import tools.jackson.databind.cfg.ContextAttributes;
import tools.jackson.databind.json.JsonMapper;

/**
 * Configurable ObjectMapper to allow customizations to the JER format produced.
 *
 * <p><b>Usage examples</b></p>
 * <pre>
 * {@code
 * // Always cache and reuse ObjectMappers
 * final static ObjectMapper customMapper = new OdeCustomJsonMapper(true);
 * final static ObjectMapper standardMapper = new OdeCustomJsonMapper(false); // or = new ObjectMapper();
 *
 * // Serialize to json with human readable bitstrings
 * var humanReadableJson = customMapper.writeValueAsString(bitstring);
 *
 * // Deserialize human readable json with human readable bitstrings
 * ExampleBitstring deserializedFromCustom = customMapper.readValue(humanReadableJson, ExampleBitstring.class);
 *
 * // Serialize to standard JER (hex)
 * var standardJerJson = standardMapper.writeValueAsString(bitstring);
 *
 * // Deserialize from standard JER
 * ExampleBitstring deserializedFromStandard = standardMapper.readValue(standardJerJson, ExampleBitstring.class);
 *
 * }
 * </pre>
 */
@Getter
public class OdeCustomJsonMapper extends JsonMapper {

  //private final static String humanReadableJsonBitstrings;
  public final static String HUMAN_READABLE_BITSTRINGS = "HUMAN_READABLE_BITSTRINGS";

  /**
   * @param humanReadableJsonBitstrings Whether to serialize/deserializer BIT STRING values to JSON
   *                                    using a non-standard human-readable format.
   */
  public OdeCustomJsonMapper(boolean humanReadableJsonBitstrings) {
    super(JsonMapper.builder()
        .defaultAttributes(
            ContextAttributes
                .getEmpty()
                .withSharedAttribute(HUMAN_READABLE_BITSTRINGS, humanReadableJsonBitstrings)));
  }

}
