package us.dot.its.jpo.asn.testgenerator;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import us.dot.its.jpo.asn.runtime.annotations.Asn1ParameterizedTypes;
import us.dot.its.jpo.asn.runtime.annotations.Asn1ParameterizedTypes.Type;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;
import us.dot.its.jpo.asn.runtime.types.Asn1Type;

@Slf4j
public class ParameterizedTypeGenerator extends RandomGenerator<Asn1Sequence> {

  Asn1ParameterizedTypes typeAnnot;

  public ParameterizedTypeGenerator(GeneratorOptions options, Asn1ParameterizedTypes typeAnnot) {
    super(options);
    this.typeAnnot = typeAnnot;
  }

  @SuppressWarnings({"unchecked"})
  @Override
  protected Asn1Sequence construct() {
    final String valuePropName = normName(typeAnnot.valueProperty());
    final Asn1ParameterizedTypes.Type[] types = typeAnnot.value();

    // Assumes the id is an int
    Map<Integer, Type> idMap = new HashMap<>();
    for (Asn1ParameterizedTypes.Type type : types) {
      idMap.put(type.intId(), type);
    }

    if (idMap.isEmpty()) {
      log.warn("There are no implementations of this type. "
          + "pdu: {}, valuePropName: {}", pdu, valuePropName);
      return null;
    }

    List<Integer> idList = idMap.keySet().stream().toList();

    log.debug("pdu: {}, valuePropName: {}, idMap: {}",
        pdu, valuePropName, idMap);



    // Choose a random id from the set
    final int numIds = idList.size();
    Random r = new Random();
    final int randIdIndex = r.nextInt(numIds);
    final int randId = idList.get(randIdIndex);
    log.debug("rand id = {}", randId);
    Asn1ParameterizedTypes.Type type = idMap.get(randId);
    log.debug("random type chosen = {}", type);
    Class<?> instanceClass = type.value();
    var gen = new SequenceGenerator(options().withPdu(instanceClass.getName()));
    var item = gen.createRandom();
    // Populate the value property
    PropertyDescriptor prop = null;
    try {
      prop = PropertyUtils.getPropertyDescriptor(item, valuePropName);
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    Class<?> valuePropType = prop.getPropertyType();
    var valueGen = getGeneratorForType((Class<Asn1Type>)valuePropType, options());
    if (valueGen != null) {
      var value = valueGen.createRandom();
      try {
        BeanUtils.setProperty(item, valuePropName, value);
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }
    return item;
  }


  @Override
  protected void populateRandom(Asn1Sequence instance) {
    // Does nothing
  }

  private String normName(String name) {
    return name.trim().replace("-", "_");
  }
}
