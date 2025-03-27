package us.dot.its.jpo.asn.testgenerator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import us.dot.its.jpo.asn.runtime.annotations.Asn1ParameterizedTypes;
import us.dot.its.jpo.asn.runtime.types.Asn1Bitstring;
import us.dot.its.jpo.asn.runtime.types.Asn1Boolean;
import us.dot.its.jpo.asn.runtime.types.Asn1Choice;
import us.dot.its.jpo.asn.runtime.types.Asn1Enumerated;
import us.dot.its.jpo.asn.runtime.types.Asn1Integer;
import us.dot.its.jpo.asn.runtime.types.Asn1ObjectIdentifier;
import us.dot.its.jpo.asn.runtime.types.Asn1OctetString;
import us.dot.its.jpo.asn.runtime.types.Asn1RelativeOID;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;
import us.dot.its.jpo.asn.runtime.types.Asn1SequenceOf;
import us.dot.its.jpo.asn.runtime.types.Asn1Type;
import us.dot.its.jpo.asn.runtime.types.IA5String;

public abstract class RandomGenerator<T extends Asn1Type> {

  protected final String pdu;
  protected final int sequenceOfLimit;
  protected final boolean regional;
  protected final Set<Class<?>> excludePdus;

  public GeneratorOptions options() {
    return new GeneratorOptions(pdu, sequenceOfLimit, regional, excludePdus);
  }

  public RandomGenerator(GeneratorOptions options) {
    this.pdu = options.pdu();
    this.sequenceOfLimit = options.limit();
    this.regional = options.regional();
    this.excludePdus = options.excludePdus();
  }

  protected abstract void populateRandom(T instance);

  protected static <T extends Asn1Type> RandomGenerator<?> getGeneratorForType(final Class<T> type,
      GeneratorOptions opts) {
    final String name = type.getName();

    final var options = opts.withPdu(name);

    try {
      Class<?> clazz = Class.forName(name);
      final Asn1ParameterizedTypes typeAnnot = clazz.getAnnotation(Asn1ParameterizedTypes.class);
      if (typeAnnot != null) {
        // this is an abstract class with a parameterized type annotation
        return new ParameterizedTypeGenerator(options, typeAnnot);
      }
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    if (Asn1Integer.class.isAssignableFrom(type)) {
      return new IntegerGenerator(options);
    } else if (Asn1Sequence.class.isAssignableFrom(type)) {
      return new SequenceGenerator(options);
    } else if (Asn1Bitstring.class.isAssignableFrom(type)) {
      return new BitstringGenerator(options);
    } else if (Asn1Enumerated.class.isAssignableFrom(type)) {
      return new EnumeratedGenerator(options);
    } else if (Asn1SequenceOf.class.isAssignableFrom(type)) {
      return new SequenceOfGenerator(options);
    } else if (Asn1Choice.class.isAssignableFrom(type)) {
      return new ChoiceGenerator(options);
    } else if (IA5String.class.isAssignableFrom(type)) {
      return new IA5StringGenerator(options);
    } else if (Asn1ObjectIdentifier.class.isAssignableFrom(type)) {
      return new ObjectIdentifierGenerator(options);
    } else if (Asn1RelativeOID.class.isAssignableFrom(type)) {
      return new RelativeOIDGenerator(options);
    } else if (Asn1OctetString.class.isAssignableFrom(type)) {
      return new OctetStringGenerator(options);
    } else if (Asn1Boolean.class.isAssignableFrom(type)) {
      return new BooleanGenerator(options);
    } else {
      System.err.printf("No RandomGenerator found for type %s%n", type.getName());
      return null;
    }
  }

  @SuppressWarnings({"unchecked"})
  protected T construct() {
    try {
      Class<?> clazz = Class.forName(pdu);
      Constructor<?> cons = clazz.getDeclaredConstructor();
      return (T) cons.newInstance();
    } catch (NoSuchMethodException nsme) {
      // There is no constructor, eg. for abstract class
      return null;
    } catch (ClassNotFoundException | InstantiationException |
             IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  public T createRandom() {
    T instance = construct();
    if (instance == null) {
      return null;
    }
    populateRandom(instance);
    return instance;
  }

  private static final XmlMapper xmlMapper = new XmlMapper();
  private static final ObjectMapper jsonMapper = new ObjectMapper();

  public String toXml(Object instance) throws JsonProcessingException {
    return xmlMapper.writeValueAsString(instance);
  }

  public String toJson(Object instance) throws JsonProcessingException {
    return jsonMapper.writeValueAsString(instance);
  }
}
