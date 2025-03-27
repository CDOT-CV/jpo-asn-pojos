package us.dot.its.jpo.asn.testgenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import us.dot.its.jpo.asn.j2735.r2024.MapData.PreemptPriorityList;
import us.dot.its.jpo.asn.runtime.annotations.Asn1Property;
import us.dot.its.jpo.asn.runtime.types.Asn1Field;
import us.dot.its.jpo.asn.runtime.types.Asn1Integer;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;
import us.dot.its.jpo.asn.runtime.types.Asn1Type;
import org.apache.commons.lang3.reflect.FieldUtils;


public class SequenceGenerator extends RandomGenerator<Asn1Sequence> {

  public SequenceGenerator(GeneratorOptions options) {
    super(options);
  }

  @Override
  protected void populateRandom(Asn1Sequence instance) {
    List<Asn1Field> fields = AsnFieldUtil.fields(instance);
    List<Asn1Field> fieldsWithValues = new ArrayList<>();
    for (Asn1Field field : fields) {

      // Ignore fields named "regional"
      if (!regional && field.name().equals("regional")) {
        fieldsWithValues.add(field);
        continue;
      }

      // Ignore certain open types containing regional/open types to produce valid messages
      // eg PreemptPriorityList for MapData
      if (excludePdus.contains(field.type())) {
        fieldsWithValues.add(field);
        continue;
      }

      RandomGenerator<?> fieldGen = getGeneratorForType(field.type(), options());
      if (fieldGen != null) {
        Asn1Type value = fieldGen.createRandom();
        if (value != null) {
          Asn1Field fieldWithValue = new Asn1Field(field.name(), value, field.optional(),
              field.tag(), field.type());
          fieldsWithValues.add(fieldWithValue);
        } else {
          fieldsWithValues.add(field);
        }
      } else {
        fieldsWithValues.add(field);
      }
    }
    AsnFieldUtil.setFields(instance, fieldsWithValues);
  }


}
