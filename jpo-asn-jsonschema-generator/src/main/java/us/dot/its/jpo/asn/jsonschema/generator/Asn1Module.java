package us.dot.its.jpo.asn.jsonschema.generator;

import static us.dot.its.jpo.asn.jsonschema.generator.Utils.construct;
import static us.dot.its.jpo.asn.jsonschema.generator.Utils.getClassFromName;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.victools.jsonschema.generator.CustomDefinition;
import com.github.victools.jsonschema.generator.CustomPropertyDefinition;
import com.github.victools.jsonschema.generator.MemberScope;
import com.github.victools.jsonschema.generator.Module;
import com.github.victools.jsonschema.generator.SchemaGenerationContext;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigPart;
import com.github.victools.jsonschema.generator.SchemaGeneratorGeneralConfigPart;
import com.github.victools.jsonschema.generator.TypeScope;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import us.dot.its.jpo.asn.runtime.types.Asn1Bitstring;
import us.dot.its.jpo.asn.runtime.types.Asn1Boolean;
import us.dot.its.jpo.asn.runtime.types.Asn1CharacterString;
import us.dot.its.jpo.asn.runtime.types.Asn1Choice;
import us.dot.its.jpo.asn.runtime.types.Asn1Enumerated;
import us.dot.its.jpo.asn.runtime.types.Asn1Integer;
import us.dot.its.jpo.asn.runtime.types.Asn1ObjectIdentifier;
import us.dot.its.jpo.asn.runtime.types.Asn1RelativeOID;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;
import us.dot.its.jpo.asn.runtime.types.Asn1SequenceOf;
import us.dot.its.jpo.asn.runtime.types.IA5String;
import us.dot.its.jpo.asn.runtime.types.Asn1Null;
import us.dot.its.jpo.asn.runtime.annotations.Asn1Property;
import us.dot.its.jpo.asn.runtime.annotations.Asn1ParameterizedTypes;
import us.dot.its.jpo.asn.runtime.types.Asn1OctetString;

public class Asn1Module implements Module {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void applyToConfigBuilder(SchemaGeneratorConfigBuilder schemaGeneratorConfigBuilder) {
    applyToConfigPart(schemaGeneratorConfigBuilder.forFields());
    applyToConfigPart(schemaGeneratorConfigBuilder.forMethods());
    applyToTypesInGeneral(schemaGeneratorConfigBuilder.forTypesInGeneral());
  }

  private void applyToConfigPart(SchemaGeneratorConfigPart<?> configPart) {
    configPart.withCustomDefinitionProvider(this::provideCustomSchemaDefinitionForMember);
  }

  private void applyToTypesInGeneral(SchemaGeneratorGeneralConfigPart configPart) {
    configPart.withTitleResolver(this::resolveTitle);
    configPart.withDescriptionResolver(this::resolveDescription);
    configPart.withCustomDefinitionProvider(this::provideCustomSchemaDefinitionForType);
  }

  private List<String> getRequiredProperties(Class<?> clazz) {
    List<String> required = new ArrayList<>();
    
    // Get all fields including those from superclasses
    for (Field field : clazz.getDeclaredFields()) {
      Asn1Property annotation = field.getAnnotation(Asn1Property.class);
      if (annotation != null && !annotation.optional()) {
        // Use the name from the annotation if present, otherwise use the field name
        String propertyName = annotation.name().isEmpty() ? field.getName() : annotation.name();
        required.add(propertyName);
      }
    }
    
    // Check superclass if it's an Asn1 type
    Class<?> superClass = clazz.getSuperclass();
    while (superClass != null && !superClass.equals(Object.class)) {
      for (Field field : superClass.getDeclaredFields()) {
        Asn1Property annotation = field.getAnnotation(Asn1Property.class);
        if (annotation != null && !annotation.optional()) {
          String propertyName = annotation.name().isEmpty() ? field.getName() : annotation.name();
          required.add(propertyName);
        }
      }
      superClass = superClass.getSuperclass();
    }
    
    return required;
  }

  private CustomPropertyDefinition provideCustomSchemaDefinitionForMember(MemberScope<?, ?> scope,
      SchemaGenerationContext context) {
    ResolvedType declaringType = scope.getDeclaringType();
    String declaredName = scope.getDeclaredName();
    ResolvedType declaredType = scope.getDeclaredType();
    System.out.printf("declaringType: %s, declaredName: %s, declaredType: %s%n",
        declaringType, declaredName, declaredType);

    // Don't do anything, use default
    return null;
  }

  private CustomDefinition provideCustomSchemaDefinitionForType(ResolvedType resolvedType,
      SchemaGenerationContext context) {    

    // Then check for specific ASN.1 types
    if (resolvedType.isInstanceOf(Asn1Integer.class)) {
      return provideIntegerDefinition(resolvedType, context);
    } else if (resolvedType.isInstanceOf(Asn1CharacterString.class)) {
      return provideCharacterStringDefinition(resolvedType, context);
    } else if (resolvedType.isInstanceOf(Asn1Bitstring.class)) {
      return provideBitstringDefinition(resolvedType, context);
    } else if (resolvedType.isInstanceOf(Asn1OctetString.class)) {
      return provideOctetStringDefinition(resolvedType, context);
    } else if (resolvedType.isInstanceOf(Asn1Enumerated.class)) {
      return provideEnumeratedDefinition(resolvedType, context);
    } else if (resolvedType.isInstanceOf(Asn1Boolean.class)) {
      return provideBooleanDefinition(resolvedType, context);
    } else if (resolvedType.isInstanceOf(Asn1ObjectIdentifier.class)
        || resolvedType.isInstanceOf(Asn1RelativeOID.class)) {
      return provideObjectIdentifierDefinition(resolvedType, context);
    } else if (resolvedType.isInstanceOf(Asn1Null.class)) {
      return provideNullDefinition(resolvedType, context);
    }

    // First check for parameterized types since they take precedence
    String typeName = resolvedType.getTypeName();
    Class<?> clazz = getClassFromName(typeName);
    if (clazz != null) {
      Asn1ParameterizedTypes typeAnnot = clazz.getAnnotation(Asn1ParameterizedTypes.class);
      if (typeAnnot != null) {
        return provideParameterizedTypeDefinition(resolvedType, typeAnnot, context);
      }
    }

    if (resolvedType.isInstanceOf(Asn1Choice.class)) {
      return provideChoiceDefinition(resolvedType, context);
    } else if (resolvedType.isInstanceOf(Asn1Sequence.class)) {
      return provideSequenceDefinition(resolvedType, context);
    } else if (resolvedType.isInstanceOf(Asn1SequenceOf.class)) {
      return provideSequenceOfDefinition(resolvedType, context);
    }

    // Use default for anything else
    return null;
  }

  private CustomDefinition provideSequenceDefinition(ResolvedType resolvedType, SchemaGenerationContext context) {
    ObjectNode node = context.getGeneratorConfig().createObjectNode();
    node.put("type", "object");
    node.put("title", resolvedType.getBriefDescription());
    node.put("description", "ASN.1 SEQUENCE Type");

    // Get the class and its fields
    Class<?> sequenceClass = resolvedType.getErasedType();
    Field[] fields = sequenceClass.getDeclaredFields();

    // Create properties object
    ObjectNode properties = node.putObject("properties");

    // Process each field
    List<String> requiredFields = new ArrayList<>();
    for (Field field : fields) {
      Asn1Property annotation = field.getAnnotation(Asn1Property.class);
      if (annotation != null) {
        // Get property name from annotation or field name
        String propertyName = annotation.name().isEmpty() ? field.getName() : annotation.name();
        
        // Add to properties
        ObjectNode property = properties.putObject(propertyName);
        
        // Get the field type
        ResolvedType fieldType = context.getTypeContext().resolve(field.getGenericType());
        
        // Generate schema for the field type
        ObjectNode fieldSchema = generateRecursiveSchema(fieldType, context);
        property.setAll(fieldSchema);

        // Add to required fields if not optional
        if (!annotation.optional()) {
          requiredFields.add(propertyName);
        }
      }
    }

    // If this is a value type from a parameterized type and has no fields,
    // try to get the schema from the generic type parameter
    if (fields.length == 0 && sequenceClass.getGenericSuperclass() != null) {
      try {
        // Get the actual type argument from the generic superclass
        ParameterizedType paramType = (ParameterizedType) sequenceClass.getGenericSuperclass();
        Class<?> valueClass = (Class<?>) paramType.getActualTypeArguments()[0];
        
        // Generate schema for the value class
        JsonSchemaGenerator gen = new JsonSchemaGenerator(valueClass);
        String schemaJson = gen.generate();
        ObjectNode valueSchema = (ObjectNode) objectMapper.readTree(schemaJson);
        
        // Create a parent field for the value class
        String parentFieldName = valueClass.getSimpleName();
        ObjectNode parentField = properties.putObject(parentFieldName);
        
        // Update the schema with the value type's properties
        if (valueSchema.has("properties")) {
          parentField.setAll(valueSchema);
        }
        
        // Add required fields from the value type
        if (valueSchema.has("required")) {
          ArrayNode valueRequired = (ArrayNode) valueSchema.get("required");
          ArrayNode required = node.putArray("required");
          required.add(parentFieldName);
        }
      } catch (Exception e) {
        // If generation fails, keep the empty schema
      }
    }

    // Add required fields array if there are any
    if (!requiredFields.isEmpty()) {
      ArrayNode required = node.putArray("required");
      requiredFields.forEach(required::add);
    }

    return new CustomDefinition(node);
  }

  private CustomDefinition provideSequenceOfDefinition(ResolvedType resolvedType, SchemaGenerationContext context) {
    ObjectNode node = context.getGeneratorConfig().createObjectNode();
    node.put("type", "array");
    node.put("title", resolvedType.getBriefDescription());
    node.put("description", "ASN.1 SEQUENCE OF Type");

    // Get the class and its type parameters
    Class<?> sequenceOfClass = resolvedType.getErasedType();
    try {
      // Get the item type from the generic superclass
      ParameterizedType paramType = (ParameterizedType) sequenceOfClass.getGenericSuperclass();
      Class<?> itemClass = (Class<?>) paramType.getActualTypeArguments()[0];
      
      // Generate schema for the item type
      ObjectNode itemsSchema = generateRecursiveSchema(context.getTypeContext().resolve(itemClass), context);
      node.set("items", itemsSchema);

      // Get size constraints from the class
      Asn1SequenceOf<?> example = (Asn1SequenceOf<?>) sequenceOfClass.getDeclaredConstructor().newInstance();
      long minSize = example.getSizeLowerBound();
      long maxSize = example.getSizeUpperBound();

      // Add size constraints
      if (minSize > 0) {
        node.put("minItems", minSize);
      }
      if (maxSize != -1) {
        node.put("maxItems", maxSize);
      }
    } catch (Exception e) {
      // If we can't get the constraints, just use the basic array type
      node.set("items", context.getGeneratorConfig().createObjectNode().put("type", "object"));
    }

    return new CustomDefinition(node);
  }

  // Used for message types that extend Asn1Null (e.g. RoadGeometryAndAttributes)
  private CustomDefinition provideNullDefinition(ResolvedType resolvedType, SchemaGenerationContext context) {
    ObjectNode node = context.getGeneratorConfig().createObjectNode()
        .put("type", "object")
        .put("description", "ASN.1 NULL Type - represents an empty value");
    
    // Add an empty properties object since it's still a valid JSON object
    node.putObject("properties");
    
    return new CustomDefinition(node);
  }

  private CustomDefinition provideParameterizedTypeDefinition(ResolvedType resolvedType,
      Asn1ParameterizedTypes typeAnnot, SchemaGenerationContext context) {
    
    ObjectNode node = context.getGeneratorConfig().createObjectNode();
    node.put("type", "object");
    node.put("title", resolvedType.getBriefDescription());
    node.put("description", "ASN.1 SEQUENCE Type with Parameterized Values");

    // Create oneOf array at the top level
    ArrayNode oneOf = node.putArray("oneOf");

    // For each possible type, create a complete object schema
    for (Asn1ParameterizedTypes.Type type : typeAnnot.value()) {
      ObjectNode typeNode = context.getGeneratorConfig().createObjectNode()
          .put("type", "object");

      // Add properties object
      ObjectNode properties = typeNode.putObject("properties");

      // Add the ID property
      ObjectNode idProp = properties.putObject(typeAnnot.idProperty());
      if (typeAnnot.idType() == Asn1ParameterizedTypes.IdType.INTEGER) {
        idProp.put("type", "integer");
        ArrayNode enumArray = idProp.putArray("enum");
        enumArray.add(type.intId());
      } else {
        idProp.put("type", "string");
        idProp.put("enum", type.stringId());
      }

      // Add the value property
      ObjectNode valueProp = properties.putObject(typeAnnot.valueProperty());
      Class<?> valueClass = type.value();
      ResolvedType valueType = context.getTypeContext().resolve(valueClass);
      
      // Generate schema for the value type
      ObjectNode valueSchema = generateRecursiveSchema(valueType, context);
      valueProp.setAll(valueSchema);

      // Add required properties
      ArrayNode required = typeNode.putArray("required");
      required.add(typeAnnot.idProperty());
      required.add(typeAnnot.valueProperty());

      oneOf.add(typeNode);
    }

    return new CustomDefinition(node);
  }

  private ObjectNode generateRecursiveSchema(ResolvedType type, SchemaGenerationContext context) {
    boolean isComplexType = type.isInstanceOf(Asn1Sequence.class) || 
                          type.isInstanceOf(Asn1SequenceOf.class) ||
                          type.isInstanceOf(Asn1Choice.class) ||
                          type.getErasedType().isAnnotationPresent(Asn1ParameterizedTypes.class);
    
    if (isComplexType) {
      try {
        JsonSchemaGenerator gen = new JsonSchemaGenerator(type.getErasedType());
        String schemaJson = gen.generate();
        // Parse the JSON string into an ObjectNode using ObjectMapper
        return (ObjectNode) objectMapper.readTree(schemaJson);
      } catch (Exception e) {
        // If generation fails, fall back to basic object type
        return context.getGeneratorConfig().createObjectNode()
            .put("type", "object");
      }
    } else {
      // For simple types, use existing custom definition logic
      CustomDefinition customDefinition = provideCustomSchemaDefinitionForType(type, context);
      if (customDefinition != null) {
        return (ObjectNode) customDefinition.getValue();
      } else {
        // If no custom definition, let the schema generator handle it
        return context.getGeneratorConfig().createObjectNode()
            .put("type", "object");
      }
    }
  }

  private CustomDefinition provideIntegerDefinition(ResolvedType resolvedType,
      SchemaGenerationContext context) {

    String typeName = resolvedType.getTypeName();
    Class<?> clazz = getClassFromName(typeName);

    Asn1Integer exampleInt = (Asn1Integer) construct(clazz);
    long lowerBound = exampleInt.getLowerBound();
    long upperBound = exampleInt.getUpperBound();

    ObjectNode node = context.getGeneratorConfig().createObjectNode()
        .put("type", "integer")
        .put("minimum", lowerBound)
        .put("maximum", upperBound);

    return new CustomDefinition(node);
  }

  private CustomDefinition provideCharacterStringDefinition(ResolvedType resolvedType,
      SchemaGenerationContext context) {

    String typeName = resolvedType.getTypeName();
    Class<?> clazz = getClassFromName(typeName);

    Asn1CharacterString example = (Asn1CharacterString) construct(clazz);
    int maxLength = example.getMaxLength();
    int minLength = example.getMinLength();

    ObjectNode node = context.getGeneratorConfig().createObjectNode()
        .put("type", "string")
        .put("minLength", minLength)
        .put("maxLength", maxLength);

    return new CustomDefinition(node);
  }

  private CustomDefinition provideBitstringDefinition(ResolvedType resolvedType,
      SchemaGenerationContext context) {
    String typeName = resolvedType.getTypeName();
    Class<?> clazz = getClassFromName(typeName);

    Asn1Bitstring example = (Asn1Bitstring) construct(clazz);
    final int minBits = example.size();
    final int minBytes = (minBits + 7) / 8;
    final int minChars = minBytes * 2;
    final int maxBits = example.upperBound();
    final int maxBytes = (maxBits + 7) / 8;
    final int maxChars = maxBytes * 2;

    ObjectNode node = context.getGeneratorConfig().createObjectNode();
    ArrayNode anyOf = node.putArray("anyOf");

    // String format
    ObjectNode stringFormat = context.getGeneratorConfig().createObjectNode();
    stringFormat.put("type", "string");
    if (example.hasExtensionMarker()) {
        stringFormat.put("pattern", String.format("^[0-9a-fA-F]{%s,}$", minChars));
    } else {
        stringFormat.put("pattern", String.format("^[0-9a-fA-F]{%s}$", minChars));
    }
    anyOf.add(stringFormat);

    // Object format
    ObjectNode objectFormat = context.getGeneratorConfig().createObjectNode();
    objectFormat.put("type", "object");
    
    ObjectNode properties = objectFormat.putObject("properties");
    
    // Value field
    ObjectNode valueNode = properties.putObject("value");
    valueNode.put("type", "string");
    if (example.hasExtensionMarker()) {
        valueNode.put("pattern", String.format("^[0-9a-fA-F]{%s,}$", minChars));
    } else {
        valueNode.put("pattern", String.format("^[0-9a-fA-F]{%s}$", minChars));
    }
    
    // Length field
    ObjectNode lengthNode = properties.putObject("length");
    lengthNode.put("type", "integer");
    lengthNode.put("minimum", minBits);
    if (!example.hasExtensionMarker()) {
        lengthNode.put("maximum", maxBits);
    }
    
    // Both fields are required
    ArrayNode required = objectFormat.putArray("required");
    required.add("value");
    required.add("length");
    
    anyOf.add(objectFormat);

    // Add description
    node.put("description", String.format("BIT STRING - hex encoded, %s%d bits%s",
        example.hasExtensionMarker() ? "minimum " : "",
        minBits,
        example.hasExtensionMarker() ? "" : ""));

    return new CustomDefinition(node);
  }

  private CustomDefinition provideOctetStringDefinition(ResolvedType resolvedType, SchemaGenerationContext context) {
    String typeName = resolvedType.getTypeName();
    Class<?> clazz = getClassFromName(typeName);

    us.dot.its.jpo.asn.runtime.types.Asn1OctetString example = (us.dot.its.jpo.asn.runtime.types.Asn1OctetString) construct(clazz);
    int minLength = example.getMinLength();
    int maxLength = example.getMaxLength();

    ObjectNode node = context.getGeneratorConfig().createObjectNode();
    node.put("type", "string");
    node.put("pattern", "^[0-9A-Fa-f]{" + (minLength * 2) + "," + (maxLength == Integer.MAX_VALUE ? "" : (maxLength * 2)) + "}$");
    node.put("description", String.format("OCTET STRING - hex encoded, min %d bytes, max %s bytes", minLength, maxLength == Integer.MAX_VALUE ? "unbounded" : Integer.toString(maxLength)));
    return new CustomDefinition(node);
  }

  private CustomDefinition provideEnumeratedDefinition(ResolvedType resolvedType,
      SchemaGenerationContext context) {
    String typeName = resolvedType.getTypeName();
    Class<?> clazz = getClassFromName(typeName);
    Object[] constants = clazz.getEnumConstants();
    List<String> names = Arrays.stream(constants).map(c -> ((Asn1Enumerated) c).getName()).toList();
    ObjectNode node = context.getGeneratorConfig().createObjectNode()
        .put("type", "string");
    ArrayNode nameArr = node.putArray("enum");
    names.forEach(nameArr::add);

    return new CustomDefinition(node);
  }

  private CustomDefinition provideBooleanDefinition(ResolvedType resolvedType, SchemaGenerationContext context) {
    ObjectNode node = context.getGeneratorConfig().createObjectNode()
        .put("type", "boolean");
    return new CustomDefinition(node);
  }

  private CustomDefinition provideObjectIdentifierDefinition(ResolvedType resolvedType, SchemaGenerationContext context) {
    ObjectNode node = context.getGeneratorConfig().createObjectNode()
        .put("type", "string");
    return new CustomDefinition(node);
  }

  private CustomDefinition provideChoiceDefinition(ResolvedType resolvedType, SchemaGenerationContext context) {
    ObjectNode node = context.getGeneratorConfig().createObjectNode();
    node.put("type", "object");
    node.put("title", resolvedType.getBriefDescription());
    node.put("description", "ASN.1 CHOICE Type - represents a union of possible types");

    // Get the class and its fields
    Class<?> clazz = resolvedType.getErasedType();
    Field[] fields = clazz.getDeclaredFields();

    // Create oneOf array to hold possible types
    ArrayNode oneOf = node.putArray("oneOf");

    // Process each field in the choice type
    for (Field field : fields) {
      Asn1Property annotation = field.getAnnotation(Asn1Property.class);
      if (annotation != null) {
        // Create a schema for this choice option
        ObjectNode choiceOption = context.getGeneratorConfig().createObjectNode();
        choiceOption.put("type", "object");

        ObjectNode properties = choiceOption.putObject("properties");
        
        // Add the field as a property
        String propertyName = annotation.name().isEmpty() ? field.getName() : annotation.name();
        ObjectNode property = properties.putObject(propertyName);
        
        // Get the field type
        ResolvedType fieldType = context.getTypeContext().resolve(field.getGenericType());
        
        // Generate schema for the field type
        ObjectNode fieldSchema = generateRecursiveSchema(fieldType, context);
        property.setAll(fieldSchema);
        
        // Add required array with just this property
        ArrayNode required = choiceOption.putArray("required");
        required.add(propertyName);
        
        oneOf.add(choiceOption);
      }
    }

    return new CustomDefinition(node, true);
  }

  private String resolveTitle(TypeScope scope) {
    var type = scope.getType();
    return type.getBriefDescription();
  }

  private String resolveDescription(TypeScope scope) {
    var type = scope.getType();
    if (type.isInstanceOf(Asn1Integer.class)) {
      return "ASN.1 INTEGER Type";
    } else if (type.isInstanceOf(Asn1Sequence.class)) {
      return "ASN.1 SEQUENCE Type";
    } else if (type.isInstanceOf(Asn1SequenceOf.class)) {
      return "ASN.1 SEQUENCE OF Type";
    } else if (type.isInstanceOf(IA5String.class)) {
      return "ASN.1 IA5String Type";
    } else if (type.isInstanceOf(Asn1Bitstring.class)) {
      return "ASN.1 BIT STRING Type";
    } else if (type.isInstanceOf(Asn1Enumerated.class)) {
      return "ASN.1 ENUMERATED Type";
    } else if (type.isInstanceOf(Asn1Boolean.class)) {
      return "ASN.1 BOOLEAN Type";
    } else if (type.isInstanceOf(Asn1ObjectIdentifier.class)) {
      return "ASN.1 OBJECT IDENTIFIER Type";
    } else if (type.isInstanceOf(Asn1RelativeOID.class)) {
      return "ASN.1 RELATIVE-OID Type";
    }
    return null;
  }
}
