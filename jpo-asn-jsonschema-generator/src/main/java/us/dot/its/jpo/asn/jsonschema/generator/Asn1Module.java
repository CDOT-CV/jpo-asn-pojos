package us.dot.its.jpo.asn.jsonschema.generator;

import static us.dot.its.jpo.asn.jsonschema.generator.Utils.construct;
import static us.dot.its.jpo.asn.jsonschema.generator.Utils.getClassFromName;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.victools.jsonschema.generator.CustomDefinition;
import com.github.victools.jsonschema.generator.CustomPropertyDefinition;
import com.github.victools.jsonschema.generator.FieldScope;
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
import us.dot.its.jpo.asn.j2735.r2024.Common.NodeListXY;
import us.dot.its.jpo.asn.j2735.r2024.MapData.RestrictionAppliesTo;
import us.dot.its.jpo.asn.j2735.r2024.REGION.Reg_ComputedLane;
import us.dot.its.jpo.asn.runtime.annotations.Asn1ParameterizedTypes;
import us.dot.its.jpo.asn.jsonschema.generator.JsonSchemaGenerator;
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
    }

    // Use default for anything else
    return null;
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

    // Add properties object
    ObjectNode properties = node.putObject("properties");

    // Add the ID property
    ObjectNode idProp = properties.putObject(typeAnnot.idProperty());
    if (typeAnnot.idType() == Asn1ParameterizedTypes.IdType.INTEGER) {
      idProp.put("type", "integer");
      // Add enum of valid integer IDs
      ArrayNode enumValues = idProp.putArray("enum");
      for (Asn1ParameterizedTypes.Type type : typeAnnot.value()) {
        enumValues.add(type.intId());
      }
    } else {
      idProp.put("type", "string");
      // Add enum of valid string IDs
      ArrayNode enumValues = idProp.putArray("enum");
      for (Asn1ParameterizedTypes.Type type : typeAnnot.value()) {
        enumValues.add(type.stringId());
      }
    }

    // Add the value property
    ObjectNode valueProp = properties.putObject(typeAnnot.valueProperty());
    valueProp.put("type", "object");

    // If this is also a sequence type, add sequence-specific properties
    if (resolvedType.isInstanceOf(Asn1Sequence.class)) {
      // Add title and description
      node.put("title", resolvedType.getBriefDescription());
      node.put("description", "ASN.1 SEQUENCE Type with Parameterized Values");
      
      // Get the class
      Class<?> clazz = resolvedType.getErasedType();
      
      // Get required properties
      List<String> required = getRequiredProperties(clazz);
      if (!required.isEmpty()) {
        ArrayNode requiredNode = node.putArray("required");
        // Add both sequence required properties and parameterized type required properties
        required.forEach(requiredNode::add);
        requiredNode.add(typeAnnot.idProperty());
        requiredNode.add(typeAnnot.valueProperty());
      } else {
        // If no sequence properties are required, still add parameterized type required properties
        ArrayNode requiredNode = node.putArray("required");
        requiredNode.add(typeAnnot.idProperty());
        requiredNode.add(typeAnnot.valueProperty());
      }
      
      // Let schema generation handle additional sequence properties
      return new CustomDefinition(node, true);
    } else {
      // For non-sequence types, just add parameterized type required properties
      ArrayNode required = node.putArray("required");
      required.add(typeAnnot.idProperty());
      required.add(typeAnnot.valueProperty());
      
      return new CustomDefinition(node);
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
        
        boolean isComplexType = fieldType.isInstanceOf(Asn1Sequence.class) || 
                              fieldType.isInstanceOf(Asn1SequenceOf.class) ||
                              fieldType.isInstanceOf(Asn1Choice.class) ||
                              fieldType.getErasedType().isAnnotationPresent(Asn1ParameterizedTypes.class);
        
        if (isComplexType) {
          // For complex types, use JsonSchemaGenerator recursively
          try {
            JsonSchemaGenerator gen = new JsonSchemaGenerator(fieldType.getErasedType());
            String schemaJson = gen.generate();
            // Parse the JSON string into an ObjectNode using ObjectMapper
            ObjectNode complexSchema = (ObjectNode) objectMapper.readTree(schemaJson);
            
            // Add the full schema into the property
            property.setAll(complexSchema);
            
          } catch (Exception e) {
            // If generation fails, fall back to basic object type
            property.put("type", "object");
          }
        } else {
          // For simple types, use existing custom definition logic
          CustomDefinition customDefinition = provideCustomSchemaDefinitionForType(fieldType, context);
          if (customDefinition != null) {
            property.setAll((ObjectNode)customDefinition.getValue());
          } else {
            // If no custom definition, let the schema generator handle it
            property.put("type", "object");
          }
        }
        
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
