package us.dot.its.jpo.asn.testgenerator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.reflect.FieldUtils;
import us.dot.its.jpo.asn.runtime.annotations.Asn1Property;
import us.dot.its.jpo.asn.runtime.types.Asn1Field;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;
import us.dot.its.jpo.asn.runtime.types.Asn1Type;

public class AsnFieldUtil {

  @SuppressWarnings({"unchecked"})
  public static List<Asn1Field> fields(Asn1Type sequence) {
    List<Asn1Field> fields = new ArrayList<>();
    for (Field field : FieldUtils.getFieldsListWithAnnotation(sequence.getClass(),
        Asn1Property.class)) {
      Object fieldValue;
      try {
        field.setAccessible(true);
        fieldValue = field.get(sequence);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
      Asn1Property annot = field.getAnnotation(Asn1Property.class);

      Asn1Field asnField = new Asn1Field(field.getName(), (Asn1Type) fieldValue, annot.optional(),
          annot.tag(), (Class<? extends Asn1Type>) field.getType());
      fields.add(asnField);

    }

    // Sort in tag order
    fields.sort(Comparator.comparingInt(Asn1Field::tag));

    return fields;
  }

  public static void setFields(Asn1Type sequence, List<Asn1Field> fields) {
    Map<String, Field> fieldMap =
        FieldUtils.getFieldsListWithAnnotation(sequence.getClass(), Asn1Property.class)
            .stream().collect(Collectors.toMap(Field::getName, field -> field));
    for (Asn1Field asnField : fields) {
      Field field = fieldMap.get(asnField.name());
      field.setAccessible(true);
      try {
        field.set(sequence, asnField.value());
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static void setField(Asn1Type sequence, Asn1Field asnField) {
    Map<String, Field> fieldMap =
        FieldUtils.getFieldsListWithAnnotation(sequence.getClass(), Asn1Property.class)
            .stream().collect(Collectors.toMap(Field::getName, field -> field));
    Field field = fieldMap.get(asnField.name());
    field.setAccessible(true);
    try {
      field.set(sequence, asnField.value());
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

}
