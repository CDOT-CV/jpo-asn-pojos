package us.dot.its.jpo.asn.jsonschema.generator;

import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SchemaGenerator {

  private final Class<?> clazz;

  public SchemaGenerator(Class<?> clazz) {
    this.clazz = clazz;
  }

  public String generate() {
    var configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_7,
        OptionPreset.PLAIN_JSON);

  }

}
