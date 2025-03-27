package us.dot.its.jpo.asn.testgenerator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import org.apache.commons.lang3.reflect.FieldUtils;
import us.dot.its.jpo.asn.runtime.annotations.Asn1Property;
import us.dot.its.jpo.asn.runtime.types.Asn1Choice;
import us.dot.its.jpo.asn.runtime.types.Asn1Field;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;
import us.dot.its.jpo.asn.runtime.types.Asn1Type;

public class ChoiceGenerator extends RandomGenerator<Asn1Choice>{

  public ChoiceGenerator(GeneratorOptions options) {
    super(options);
  }

  @Override
  protected void populateRandom(Asn1Choice instance) {
    List<Asn1Field> fields = AsnFieldUtil.fields(instance);

    // Exclude choices named "regional"
    List<Asn1Field> filteredFields = fields;
    if (!regional) {
      filteredFields = fields.stream().filter(
          field -> !field.name().equals("regional")
      ).toList();
    }

    // Choose a random one
    final int numChoices = filteredFields.size();
    Random r = new Random();
    final int choiceIndex = r.nextInt(numChoices);
    Asn1Field choice = filteredFields.get(choiceIndex);
    RandomGenerator<?> fieldGen = getGeneratorForType(choice.type(), options());

    // Populate the choice
    if (fieldGen != null) {
      Asn1Type value = fieldGen.createRandom();
      Asn1Field fieldWithValue = new Asn1Field(choice.name(), value, choice.optional(),
          choice.tag(), choice.type());
      AsnFieldUtil.setField(instance, fieldWithValue);
    }
  }




}
