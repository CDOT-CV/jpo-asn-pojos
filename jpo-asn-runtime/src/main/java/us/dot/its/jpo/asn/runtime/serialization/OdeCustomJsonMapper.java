package us.dot.its.jpo.asn.runtime.serialization;

import tools.jackson.databind.ObjectMapper;
import lombok.Getter;

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
public class OdeCustomJsonMapper extends ObjectMapper {

  private final boolean humanReadableJsonBitstrings;

  /**
   * @param humanReadableJsonBitstrings Whether to serialize/deserializer BIT STRING values to JSON
   *                                    using a non-standard human-readable format.
   */
  public OdeCustomJsonMapper(boolean humanReadableJsonBitstrings) {
    this.humanReadableJsonBitstrings = humanReadableJsonBitstrings;
  }

}
